package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.chat.dto.MessageDetailsDto;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.Message;
import ru.greenpix.messenger.chat.entity.Message_;
import ru.greenpix.messenger.chat.entity.PrivateChat;
import ru.greenpix.messenger.chat.exception.ChatNotFoundException;
import ru.greenpix.messenger.chat.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.chat.integration.users.client.UsersClient;
import ru.greenpix.messenger.chat.mapper.MessageMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.MessageRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.MessageService;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;
import ru.greenpix.messenger.common.specification.BaseSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatRepository chatRepository;
    private final PrivateChatRepository privateChatRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UsersClient usersClient;
    private final FriendsClient friendsClient;

    @Override
    public @NotNull Page<MessageDto> getMessages(@NotNull UUID userId, int page, int size, String textFilter) {
        Specification<Message> spec = BaseSpecification.containsIgnoreCase(Message_.text, textFilter);

        // TODO textFilter в приложениях

        return messageRepository.findAll(spec, PageRequest.of(page, size))
                .map(e -> messageMapper.toDto(e, e.getChat().getId().toString() /*TODO*/));
    }

    @Transactional
    @Override
    public @NotNull List<MessageDetailsDto> getChatMessages(@NotNull UUID requesterId, @NotNull UUID chatId) {
        if (!chatRepository.existsIdAndMember(chatId, requesterId)) {
            throw new ChatNotFoundException();
        }

        List<Message> messages = messageRepository.findAllByChatId(chatId);
        Map<Message, UserIntegrationDto> messageOwnerMap = toMessageOwnerMap(messages);

        return messageOwnerMap.entrySet()
                .stream()
                .map(e -> messageMapper.toDetailsDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void sendPrivateMessage(@NotNull UUID senderId, @NotNull UUID receiverId, @NotNull SendingMessageDto dto) {
        Set<UUID> members = Set.of(senderId, receiverId);
        PrivateChat chat = privateChatRepository.findByMemberIds(members).orElse(null);

        if (chat == null) {
            chat = new PrivateChat();
            chat.setMemberIds(members);
            chat = privateChatRepository.save(chat);
        }

        Message message = createMessage(senderId, chat, dto);
        messageRepository.save(message);
    }

    @Transactional
    @Override
    public void sendGroupMessage(@NotNull UUID senderId, @NotNull UUID chatId, @NotNull SendingMessageDto dto) {
        Chat chat = chatRepository.findIdAndMember(chatId, senderId).orElseThrow(ChatNotFoundException::new);
        Message message = createMessage(senderId, chat, dto);
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

    private Message createMessage(UUID senderId, Chat chat, SendingMessageDto dto) {
        Message message = messageMapper.toMessageEntity(dto);
        message.setCreationTimestamp(LocalDateTime.now());
        message.setSenderId(senderId);
        message.setChat(chat);
        return message;
    }
}
