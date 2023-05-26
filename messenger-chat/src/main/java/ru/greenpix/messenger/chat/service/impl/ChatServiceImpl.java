package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.ChatMember;
import ru.greenpix.messenger.chat.entity.GroupChat;
import ru.greenpix.messenger.chat.entity.Message;
import ru.greenpix.messenger.chat.exception.ChatNotFoundException;
import ru.greenpix.messenger.chat.exception.IllegalChatTypeException;
import ru.greenpix.messenger.chat.exception.UploadFileFailedException;
import ru.greenpix.messenger.chat.exception.UserBlockedException;
import ru.greenpix.messenger.chat.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.chat.integration.storage.client.FileStorageClient;
import ru.greenpix.messenger.chat.integration.users.client.UsersClient;
import ru.greenpix.messenger.chat.mapper.ChatMapper;
import ru.greenpix.messenger.chat.mapper.ChatMemberMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.GroupChatRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.ChatService;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;
import ru.greenpix.messenger.common.exception.UserNotFoundException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final FileStorageClient fileStorageClient;
    private final UsersClient usersClient;
    private final ChatMapper chatMapper;
    private final ChatMemberMapper chatMemberMapper;
    private final Logger logger;

    @Override
    public @NotNull Page<ChatDto> getAccessibleChat(@NotNull UUID userId, int page, int size, String nameFilter) {
        logger.trace("User {} is requesting accessible chats (page={}, size={}, nameFilter={})", userId, page, size, nameFilter);

        String pattern = "%" + (nameFilter == null ? "" : nameFilter) + "%";
        return chatRepository.findAllWithLastMessageOrderByLastMessageTime(userId, pattern, PageRequest.of(page, size))
                .map(tuple -> {
                    String name = tuple.get("name", String.class);
                    Chat chat = tuple.get("chat", Chat.class);
                    Message message = tuple.get("message", Message.class);

                    return chatMapper.toDto(chat, name, message);
                });
    }

    @Override
    public @NotNull ChatDetailsDto getChat(@NotNull UUID requesterId, @NotNull UUID chatId) {
        logger.trace("User {} is requesting chat {}", requesterId, chatId);
        Chat chat = chatRepository.findIdAndMember(chatId, requesterId).orElseThrow(ChatNotFoundException::new);

        String chatName;
        UUID chatAvatarId;
        UUID chatAdminId;

        if (chat instanceof GroupChat) {
            chatName = ((GroupChat) chat).getName();
            chatAvatarId = ((GroupChat) chat).getAvatarId();
            chatAdminId = ((GroupChat) chat).getAdminUserId();
        }
        else {
            ChatMember member = chat.getMembers()
                    .stream()
                    .filter(e -> !e.getId().getUserId().equals(requesterId))
                    .findAny()
                    // Эта ошибка не должна появляться, т.к. в приватном чате всегда 2 участника.
                    // Однако на всякий случай пусть будет orElseThrow() вместе get()
                    .orElseThrow(() -> new RuntimeException("Cannot find interlocutor"));

            chatName = member.getMemberName();
            chatAvatarId = member.getMemberAvatarId();
            chatAdminId = null;
        }
        return chatMapper.toDetailsDto(chat, chatName, chatAvatarId, chatAdminId);
    }

    @Override
    public void createChat(@NotNull UUID creatorId, @NotNull ModificationChatDto modificationChatDto,
                           @Nullable MultipartFile avatar) {
        logger.debug("User {} is creating chat {}", creatorId, modificationChatDto);
        Chat chat = saveChat(new GroupChat(), creatorId, modificationChatDto, avatar);
        logger.info("User {} created chat {}", creatorId, chat.getId());
    }

    @Transactional
    @Override
    public void updateChat(@NotNull UUID creatorId, @NotNull UUID chatId,
                           @NotNull ModificationChatDto modificationChatDto,
                           @Nullable MultipartFile avatar) {
        logger.debug("User {} is updating chat {} {}", creatorId, chatId, modificationChatDto);
        Chat chat = chatRepository.findIdAndMember(chatId, creatorId).orElseThrow(ChatNotFoundException::new);

        if (!(chat instanceof GroupChat)) {
            throw new IllegalChatTypeException();
        }

        saveChat((GroupChat) chat, creatorId, modificationChatDto, avatar);
        logger.info("User {} updated chat {}", creatorId, chat.getId());
    }

    private Chat saveChat(GroupChat chat, UUID creatorId, ModificationChatDto dto, MultipartFile avatar) {
        List<UUID> members = new ArrayList<>(dto.getMembers());
        if (!members.contains(creatorId)) {
            members.add(creatorId);
        }

        List<UserIntegrationDto> dtoList = usersClient.getUsers(members);

        if (dtoList.size() != members.size()) {
            throw new UserNotFoundException();
        }

        // Тут бы в один запрос оптимизировать, но я не успел
        for (UUID id : dto.getMembers()) {
            if (friendsClient.isBlockedByUser(id, creatorId)) {
                throw new UserBlockedException();
            }
        }

        UUID avatarId = null;
        if (avatar != null) {
            Optional<UUID> fileId = fileStorageClient.uploadFile(avatar);
            if (fileId.isEmpty()) {
                throw new UploadFileFailedException();
            }
            avatarId = fileId.get();
        }

        chat.setName(dto.getName());
        chat.setAdminUserId(creatorId);
        chat.setAvatarId(avatarId);
        chat.setCreationTimestamp(LocalDateTime.now(clock));
        chat.getMembers().clear();
        chat.getMembers().addAll(dtoList.stream().map(e -> chatMemberMapper.toChatMember(chat, e)).collect(Collectors.toSet()));
        return chatRepository.save(chat);
    }
}
