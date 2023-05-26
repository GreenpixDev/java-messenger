package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import ru.greenpix.messenger.chat.mapper.ChatMemberMapper;
import ru.greenpix.messenger.chat.mapper.MessageMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.MessageRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.MessageService;
import ru.greenpix.messenger.chat.settings.NotificationSettings;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;
import ru.greenpix.messenger.common.exception.UserNotFoundException;

import java.time.Clock;
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
    private final ChatMemberMapper chatMemberMapper;
    private final UsersClient usersClient;
    private final FriendsClient friendsClient;
    private final FileStorageClient fileStorageClient;
    private final NotificationProducer notificationProducer;
    private final NotificationSettings notificationSettings;
    private final Clock clock;
    private final Logger logger;

    @Override
    public @NotNull Page<MessageDto> getMessages(@NotNull UUID userId, int page, int size, String textFilter) {
        logger.trace("User {} is requesting all messages (page={}, size={}, textFilter={})", userId, page, size, textFilter);

        String pattern = "%" + textFilter + "%";
        Sort sort = Sort.by(Sort.Direction.DESC, Message_.CREATION_TIMESTAMP);
        return messageRepository.findAllWithChatNameAndAttachmentNames(userId, pattern, PageRequest.of(page, size, sort))
                .map(e -> {
                    Message message = e.get("message", Message.class);
                    String chatName = e.get("chatName", String.class);
                    String attachmentName = e.get("attachmentName", String.class);

                    return messageMapper.toDto(message, chatName, attachmentName);
                });
    }

    // Подумал, что лучше возвращать страницу, а не список, но не хватило времени переделать
    @Transactional
    @Override
    public @NotNull List<MessageDetailsDto> getChatMessages(@NotNull UUID requesterId, @NotNull UUID chatId) {
        if (!chatRepository.existsIdAndMember(chatId, requesterId)) {
            throw new ChatNotFoundException();
        }
        logger.trace("User {} is requesting messages in chat {}", requesterId, chatId);

        Sort sort = Sort.by(Sort.Direction.DESC, Message_.CREATION_TIMESTAMP);
        Page<Message> messages = messageRepository.findAllByChatId(chatId, Pageable.unpaged());
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
        logger.debug("User {} is sending private message to user {} with {} attachments (text = {})",
                senderId, receiverId, files.length, dto.getText());

        PrivateChat chat = privateChatRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElse(null);

        if (friendsClient.isBlockedByUser(receiverId, senderId)) {
            throw new UserBlockedException();
        }
        if (!friendsClient.isFriend(senderId, receiverId)) {
            throw new UserNotFriendException();
        }

        if (chat == null) {
            List<UserIntegrationDto> dtoList = usersClient.getUsers(Set.of(senderId, receiverId));
            if (dtoList.size() != 2) {
                // Конечно, такая ошибка не должна произойти, т.к. сервис друзей проверяет это.
                // Но пусть будет
                throw new UserNotFoundException();
            }

            final PrivateChat finalChat = new PrivateChat();
            chat = finalChat;
            chat.setCreationTimestamp(LocalDateTime.now(clock));
            chat.setMembers(dtoList.stream().map(e -> chatMemberMapper.toChatMember(finalChat, e)).collect(Collectors.toSet()));
            chat = privateChatRepository.save(chat);
        }

        Message message = createMessage(senderId, chat, dto, files);
        messageRepository.save(message);

        notificationProducer.sendNotification(new NotificationAmqpDto(
                receiverId,
                NotificationType.NEW_PRIVATE_MESSAGE,
                notificationSettings.getNewPrivateMessage()
        ));

        logger.info("User {} sent private message to user {}", senderId, receiverId);
    }

    @Transactional
    @Override
    public void sendGroupMessage(@NotNull UUID senderId, @NotNull UUID chatId,
                                 @NotNull SendingMessageDto dto, @NotNull MultipartFile[] files) {
        logger.debug("User {} is sending group message to chat {} with {} attachments (text = {})",
                senderId, chatId, files.length, dto.getText());

        Chat chat = chatRepository.findIdAndMember(chatId, senderId).orElseThrow(ChatNotFoundException::new);
        Message message = createMessage(senderId, chat, dto, files);
        messageRepository.save(message);

        logger.info("User {} sent group message to chat {}", senderId, chatId);
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
