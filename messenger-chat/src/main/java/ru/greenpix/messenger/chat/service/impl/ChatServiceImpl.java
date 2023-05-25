package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.GroupChat;
import ru.greenpix.messenger.chat.exception.ChatNotFoundException;
import ru.greenpix.messenger.chat.exception.IllegalChatTypeException;
import ru.greenpix.messenger.chat.mapper.ChatMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.GroupChatRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.ChatService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final Clock clock;
    private final ChatRepository chatRepository;
    private final GroupChatRepository groupChatRepository;
    private final PrivateChatRepository privateChatRepository;
    private final ChatMapper mapper;

    @Override
    public @NotNull Page<ChatDto> getAccessibleChat(@NotNull UUID userId, int page, int size, String nameFilter) {
        /*Specification<GroupChat> spec = BaseSpecification.containsIgnoreCase(GroupChat_.name, nameFilter);
        Specification<PrivateChat> spec = BaseSpecification.containsIgnoreCase(GroupChat_.name, nameFilter);

        List<Chat> chats = new ArrayList<>();
        chats.addAll(groupChatRepository.findAll(spec, PageRequest.of(page, size)));
        chats.addAll(privateChatRepository.findAll(spec, PageRequest.of(page, size)));

        return
                .map(mapper::toDto);*/
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ChatDetailsDto getChat(@NotNull UUID requesterId, @NotNull UUID chatId) {
        Chat chat = chatRepository.findIdAndMember(chatId, requesterId).orElseThrow(ChatNotFoundException::new);

        return mapper.toDetailsDto(chat);
    }

    @Override
    public void createChat(@NotNull UUID creatorId, @NotNull ModificationChatDto modificationChatDto) {
        if (!modificationChatDto.getMembers().contains(creatorId)) {
            modificationChatDto.getMembers().add(creatorId);
        }

        GroupChat chat = mapper.toGroupChatEntity(modificationChatDto);
        chat.setAdminUserId(creatorId);
        chat.setCreationTimestamp(LocalDateTime.now(clock));
        chatRepository.save(chat);
    }

    @Transactional
    @Override
    public void updateChat(@NotNull UUID creatorId, @NotNull UUID chatId, @NotNull ModificationChatDto modificationChatDto) {
        Chat chat = chatRepository.findIdAndMember(chatId, creatorId).orElseThrow(ChatNotFoundException::new);

        if (!(chat instanceof GroupChat)) {
            throw new IllegalChatTypeException();
        }

        if (!modificationChatDto.getMembers().contains(creatorId)) {
            modificationChatDto.getMembers().add(creatorId);
        }

        GroupChat groupChat = mapper.partialUpdateGroupChatEntity(modificationChatDto, (GroupChat) chat);
        chatRepository.save(chat);
    }

}
