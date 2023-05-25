package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.ChatMember;
import ru.greenpix.messenger.chat.entity.ChatMemberId;
import ru.greenpix.messenger.chat.entity.GroupChat;
import ru.greenpix.messenger.chat.entity.Message;
import ru.greenpix.messenger.chat.exception.ChatNotFoundException;
import ru.greenpix.messenger.chat.exception.IllegalChatTypeException;
import ru.greenpix.messenger.chat.exception.UserBlockedException;
import ru.greenpix.messenger.chat.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.chat.integration.users.client.UsersClient;
import ru.greenpix.messenger.chat.mapper.ChatMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.GroupChatRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.ChatService;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;
import ru.greenpix.messenger.common.exception.UserNotFoundException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final Clock clock;
    private final ChatRepository chatRepository;
    private final GroupChatRepository groupChatRepository;
    private final PrivateChatRepository privateChatRepository;
    private final FriendsClient friendsClient;
    private final UsersClient usersClient;
    private final ChatMapper mapper;

    @Override
    public @NotNull Page<ChatDto> getAccessibleChat(@NotNull UUID userId, int page, int size, String nameFilter) {
        String pattern = "%" + (nameFilter == null ? "" : nameFilter) + "%";
        return chatRepository.findAllWithLastMessage(userId, pattern, PageRequest.of(page, size))
                .map(tuple -> {
                    String name = tuple.get("name", String.class);
                    Chat chat = tuple.get("chat", Chat.class);
                    Message message = tuple.get("message", Message.class);

                    return new ChatDto(
                            chat.getId(),
                            name,
                            message.getText(),
                            message.getCreationTimestamp(),
                            message.getSenderId()
                    );
                });
    }

    @Override
    public @NotNull ChatDetailsDto getChat(@NotNull UUID requesterId, @NotNull UUID chatId) {
        Chat chat = chatRepository.findIdAndMember(chatId, requesterId).orElseThrow(ChatNotFoundException::new);

        if (chat instanceof GroupChat) {
            return mapper.toDetailsDto((GroupChat) chat);
        }
        else {
            ChatMember member = chat.getMembers()
                    .stream()
                    .filter(e -> !e.getId().getUserId().equals(requesterId))
                    .findAny()
                    .get();

            return new ChatDetailsDto(
                    member.getMemberName(),
                    member.getMemberAvatarId(),
                    null,
                    chat.getCreationTimestamp()
            );
        }
    }

    @Override
    public void createChat(@NotNull UUID creatorId, @NotNull ModificationChatDto modificationChatDto) {
        saveChat(new GroupChat(), creatorId, modificationChatDto);
    }

    @Transactional
    @Override
    public void updateChat(@NotNull UUID creatorId, @NotNull UUID chatId, @NotNull ModificationChatDto modificationChatDto) {
        Chat chat = chatRepository.findIdAndMember(chatId, creatorId).orElseThrow(ChatNotFoundException::new);

        if (!(chat instanceof GroupChat)) {
            throw new IllegalChatTypeException();
        }

        saveChat((GroupChat) chat, creatorId, modificationChatDto);
    }

    private void saveChat(GroupChat chat, UUID creatorId, ModificationChatDto dto) {
        List<UUID> members = dto.getMembers();
        if (!members.contains(creatorId)) {
            members.add(creatorId);
        }

        List<UserIntegrationDto> dtoList = usersClient.getUsers(members);

        if (dtoList.size() != members.size()) {
            throw new UserNotFoundException();
        }

        // TODO оптимизировать в один запрос
        for (UUID id : dto.getMembers()) {
            if (friendsClient.isBlockedByUser(id, creatorId)) {
                throw new UserBlockedException();
            }
        }


        chat.setName(dto.getName());
        chat.setAdminUserId(creatorId);
        chat.setCreationTimestamp(LocalDateTime.now(clock));
        chat.setMembers(dtoList.stream().map(e -> toChatMember(chat, e)).collect(Collectors.toSet()));
        chatRepository.save(chat);
    }

    // TODO вынести в маппер
    private ChatMember toChatMember(Chat chat, UserIntegrationDto dto) {
        ChatMember chatMember = new ChatMember();
        chatMember.setId(new ChatMemberId(chat, dto.getId()));
        chatMember.setMemberName(dto.getFullName());
        chatMember.setMemberAvatarId(dto.getAvatarId());
        return chatMember;
    }
}
