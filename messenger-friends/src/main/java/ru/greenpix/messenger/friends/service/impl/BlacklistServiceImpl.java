package ru.greenpix.messenger.friends.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Relationship;
import ru.greenpix.messenger.friends.exception.AdditionFriendException;
import ru.greenpix.messenger.friends.exception.DeletionFriendException;
import ru.greenpix.messenger.friends.exception.FriendNotFoundException;
import ru.greenpix.messenger.friends.repository.BlacklistRepository;
import ru.greenpix.messenger.friends.service.BlacklistService;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final Clock clock;

    @Override
    public @NotNull Page<BlockedUser> getBlockedUserPage(
            @NotNull UUID targetUserId,
            int page,
            int size,
            @NotNull String fullNameFilter
    ) {
        return blacklistRepository.findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
                PageRequest.of(page, size),
                targetUserId,
                "%" + fullNameFilter + "%"
        );
    }

    @Override
    public @NotNull BlockedUser getBlockedUser(@NotNull UUID targetUserId, @NotNull UUID friendUserId) {
        return blacklistRepository.findById(new Relationship(targetUserId, friendUserId))
                .orElseThrow(FriendNotFoundException::new);
    }

    @Transactional
    @Override
    public void addBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        Relationship relationship = new Relationship(targetUserId, blockedUserId);
        BlockedUser blockedUser = blacklistRepository.findById(relationship).orElse(null);

        if (blockedUser != null) {
            if (blockedUser.getDeletionDate() == null) {
                throw new AdditionFriendException();
            }
            blockedUser.setDeletionDate(null);
        }
        else {
            blockedUser = new BlockedUser();
            blockedUser.setRelationship(relationship);
            blockedUser.setFullName("Test"); // TODO
        }
        blockedUser.setAdditionDate(LocalDate.now(clock));

        blacklistRepository.save(blockedUser);
    }

    @Transactional
    @Override
    public void deleteBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        BlockedUser blockedUser = blacklistRepository.findById(new Relationship(targetUserId, blockedUserId))
                .orElseThrow(DeletionFriendException::new);

        if (blockedUser.getDeletionDate() != null) {
            throw new DeletionFriendException();
        }

        blockedUser.setDeletionDate(LocalDate.now(clock));
        blacklistRepository.save(blockedUser);
    }

    @Override
    public boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        return blacklistRepository.existsByRelationshipAndDeletionDateNull(new Relationship(targetUserId, blockedUserId));
    }
}
