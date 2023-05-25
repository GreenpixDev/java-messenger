package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.amqp.dto.NotificationType;
import ru.greenpix.messenger.amqp.producer.producer.NotificationProducer;
import ru.greenpix.messenger.chat.dto.MessageDetailsDto;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;
import ru.greenpix.messenger.chat.entity.Attachment;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.Message;
import ru.greenpix.messenger.chat.entity.Message_;
import ru.greenpix.messenger.chat.entity.PrivateChat;
import ru.greenpix.messenger.chat.exception.ChatNotFoundException;
import ru.greenpix.messenger.chat.exception.UploadFileFailedException;
import ru.greenpix.messenger.chat.exception.UserBlockedException;
import ru.greenpix.messenger.chat.exception.UserNotFriendException;
import ru.greenpix.messenger.chat.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.chat.integration.storage.client.FileStorageClient;
import ru.greenpix.messenger.chat.integration.users.client.UsersClient;
import ru.greenpix.messenger.chat.mapper.MessageMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.MessageRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.MessageService;
import ru.greenpix.messenger.chat.settings.NotificationSettings;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;
import ru.greenpix.messenger.common.specification.BaseSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatRepository chatRepository;
    private final PrivateChatRepository privateChatRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UsersClient usersClient;
    private final FriendsClient friendsClient;
    private final FileStorageClient fileStorageClient;
    private final NotificationProducer notificationProducer;
    private final NotificationSettings notificationSettings;

    @Override
    public @NotNull Page<MessageDto> getMessages(@NotNull UUID userId, int page, int size, String textFilter) {
        Specification<Message> spec = BaseSpecification.containsIgnoreCase(Message_.text, textFilter);

        // TODO textFilter в приложениях

        Sort sort = Sort.by(Sort.Direction.DESC, Message_.CREATION_TIMESTAMP);

        return messageRepository.findAll(spec, PageRequest.of(page, size, sort))
                .map(e -> messageMapper.toDto(e, e.getChat().getId().toString() /*TODO*/));
    }

    @Transactional
    @Override
    public @NotNull List<MessageDetailsDto> getChatMessages(@NotNull UUID requesterId, @NotNull UUID chatId) {
        if (!chatRepository.existsIdAndMember(chatId, requesterId)) {
            throw new ChatNotFoundException();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, Message_.CREATION_TIMESTAMP);
        Page<Message> messages = messageRepository.findAllByChatId(chatId, PageRequest.of(0, 100, sort));
        Map<Message, UserIntegrationDto> messageOwnerMap = toMessageOwnerMap(messages.getContent());

        return messageOwnerMap.entrySet()
                .stream()
                .map(e -> messageMapper.toDetailsDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void sendPrivateMessage(@NotNull UUID senderId, @NotNull UUID receiverId,
                                   @NotNull SendingMessageDto dto, @NotNull MultipartFile[] files) {
        PrivateChat chat = privateChatRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElse(null);

        if (friendsClient.isBlockedByUser(receiverId, senderId)) {
            throw new UserBlockedException();
        }
        if (!friendsClient.isFriend(senderId, receiverId)) {
            throw new UserNotFriendException();
        }

        if (chat == null) {
            chat = new PrivateChat();
            // TODO chat.setMemberIds(Set.of(senderId, receiverId));
            chat = privateChatRepository.save(chat);
        }

        Message message = createMessage(senderId, chat, dto, files);
        messageRepository.save(message);

        notificationProducer.sendNotification(new NotificationAmqpDto(
                receiverId,
                NotificationType.NEW_PRIVATE_MESSAGE,
                notificationSettings.getNewPrivateMessage()
        ));
    }

    @Transactional
    @Override
    public void sendGroupMessage(@NotNull UUID senderId, @NotNull UUID chatId,
                                 @NotNull SendingMessageDto dto, @NotNull MultipartFile[] files) {
        Chat chat = chatRepository.findIdAndMember(chatId, senderId).orElseThrow(ChatNotFoundException::new);
        Message message = createMessage(senderId, chat, dto, files);
        messageRepository.save(message);
    }

    private Map<Message, UserIntegrationDto> toMessageOwnerMap(List<Message> messages) {
        Set<UUID> senderIds = messages.stream().map(Message::getSenderId).collect(Collectors.toSet());
        Map<UUID, UserIntegrationDto> users = usersClient.getUsers(senderIds)
                .stream()
                .collect(Collectors.toMap(UserIntegrationDto::getId, Function.identity()));
        return messages
                .stream()
                .collect(Collectors.toMap(Function.identity(), e -> users.get(e.getSenderId())));
    }

    private Message createMessage(UUID senderId, Chat chat, SendingMessageDto dto, MultipartFile[] files) {
        Message message = messageMapper.toMessageEntity(dto);
        message.setCreationTimestamp(LocalDateTime.now());
        message.setSenderId(senderId);
        message.setChat(chat);

        Set<Attachment> attachments = Stream.of(files)
                .map(file -> createAttachment(message, file))
                .collect(Collectors.toSet());
        message.setAttachments(attachments);

        return message;
    }

    private Attachment createAttachment(Message message, MultipartFile file) {
        Optional<UUID> fileId = fileStorageClient.uploadFile(file);
        if (fileId.isEmpty()) {
            throw new UploadFileFailedException();
        }

        Attachment attachment = new Attachment();
        attachment.setMessage(message);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileSize(file.getSize());
        attachment.setFileId(fileId.get());
        return attachment;
    }
}
