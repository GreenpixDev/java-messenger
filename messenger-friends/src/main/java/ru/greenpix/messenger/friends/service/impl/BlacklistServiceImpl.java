package ru.greenpix.messenger.friends.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.common.exception.UserNotFoundException;
import ru.greenpix.messenger.common.specification.BaseSpecification;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.BlockedUser_;
import ru.greenpix.messenger.friends.entity.Relationship;
import ru.greenpix.messenger.friends.exception.AdditionBlockedUserException;
import ru.greenpix.messenger.friends.exception.BlockedUserNotFoundException;
import ru.greenpix.messenger.friends.exception.DeletionBlockedUserException;
import ru.greenpix.messenger.friends.integration.users.client.UsersClient;
import ru.greenpix.messenger.friends.repository.BlacklistRepository;
import ru.greenpix.messenger.friends.service.BlacklistService;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final Clock clock;
    private final UsersClient usersClient;

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
    public @NotNull BlockedUser getBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        return blacklistRepository.findById(new Relationship(targetUserId, blockedUserId))
                .orElseThrow(BlockedUserNotFoundException::new);
    }

    @Transactional
    @Override
    public void addBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        Relationship relationship = new Relationship(targetUserId, blockedUserId);
        BlockedUser blockedUser = blacklistRepository.findById(relationship).orElse(null);

        if (blockedUser != null) {
            if (blockedUser.getDeletionDate() == null) {
                throw new AdditionBlockedUserException();
            }
            blockedUser.setDeletionDate(null);
        }
        else {
            String fullName = usersClient.getUserFullName(blockedUserId)
                    .orElseThrow(UserNotFoundException::new);

            blockedUser = new BlockedUser();
            blockedUser.setRelationship(relationship);
            blockedUser.setFullName(fullName);
        }
        blockedUser.setAdditionDate(LocalDate.now(clock));

        blacklistRepository.save(blockedUser);
    }

    @Transactional
    @Override
    public void deleteBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        BlockedUser blockedUser = blacklistRepository.findById(new Relationship(targetUserId, blockedUserId))
                .orElseThrow(DeletionBlockedUserException::new);

        if (blockedUser.getDeletionDate() != null) {
            throw new DeletionBlockedUserException();
        }

        blockedUser.setDeletionDate(LocalDate.now(clock));
        blacklistRepository.save(blockedUser);
    }

    @Override
    public @NotNull Page<BlockedUser> getBlockedUserPage(@NotNull UUID targetUserId, int page, int size, @NotNull BlockedUserSearchDto searchDto) {
        List<Specification<BlockedUser>> specs = new ArrayList<>();
        if (searchDto.getFullName() != null) {
            specs.add(BaseSpecification.containsIgnoreCase(BlockedUser_.fullName, searchDto.getFullName()));
        }
        if (searchDto.getAdditionDate() != null) {
            specs.add(BaseSpecification.equal(BlockedUser_.additionDate, searchDto.getAdditionDate()));
        }

        return blacklistRepository.findAll(BaseSpecification.all(specs), PageRequest.of(page, size));
    }

    @Override
    public boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        return blacklistRepository.existsByRelationshipAndDeletionDateNull(new Relationship(targetUserId, blockedUserId));
    }
}