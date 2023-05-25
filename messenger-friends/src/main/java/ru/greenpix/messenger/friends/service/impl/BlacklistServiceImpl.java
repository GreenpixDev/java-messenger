package ru.greenpix.messenger.friends.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.amqp.dto.NotificationType;
import ru.greenpix.messenger.amqp.producer.producer.NotificationProducer;
import ru.greenpix.messenger.common.exception.UserNotFoundException;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Relationship;
import ru.greenpix.messenger.friends.exception.AdditionBlockedUserException;
import ru.greenpix.messenger.friends.exception.AdditionYourselfAsBlockedUserException;
import ru.greenpix.messenger.friends.exception.BlockedUserNotFoundException;
import ru.greenpix.messenger.friends.exception.DeletionBlockedUserException;
import ru.greenpix.messenger.friends.integration.users.client.UsersClient;
import ru.greenpix.messenger.friends.mapper.FilterMapper;
import ru.greenpix.messenger.friends.repository.BlacklistRepository;
import ru.greenpix.messenger.friends.service.BlacklistService;
import ru.greenpix.messenger.friends.settings.NotificationSettings;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final Clock clock;
    private final UsersClient usersClient;
    private final FilterMapper mapper;
    private final NotificationProducer notificationProducer;
    private final NotificationSettings notificationSettings;

    @Override
    public @NotNull Page<BlockedUser> getBlockedUserPage(
            @NotNull UUID targetUserId,
            int page,
            int size,
            @NotNull String fullNameFilter
    ) {
        log.trace("User {} is requesting blocked user list (page={}, size={}, fullNameFilter={})", targetUserId, page, size, fullNameFilter);
        return blacklistRepository.findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
                PageRequest.of(page, size),
                targetUserId,
                "%" + fullNameFilter + "%"
        );
    }

    @Override
    public @NotNull BlockedUser getBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        log.trace("User {} is requesting details of blocked user {}", targetUserId, blockedUserId);
        return blacklistRepository.findById(new Relationship(targetUserId, blockedUserId))
                .orElseThrow(BlockedUserNotFoundException::new);
    }

    @Transactional
    @Override
    public void addBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        log.trace("User {} is adding blocked user {}", targetUserId, blockedUserId);

        if (targetUserId.equals(blockedUserId)) {
            throw new AdditionYourselfAsBlockedUserException();
        }

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
        log.trace("User {} added blocked user {}", targetUserId, blockedUserId);

        notificationProducer.sendNotification(new NotificationAmqpDto(
                blockedUserId,
                NotificationType.BLOCKED,
                notificationSettings.getBlock()
        ));
    }

    @Transactional
    @Override
    public void deleteBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        log.trace("User {} is removing blocked user {}", targetUserId, blockedUserId);
        BlockedUser blockedUser = blacklistRepository.findById(new Relationship(targetUserId, blockedUserId))
                .orElseThrow(DeletionBlockedUserException::new);

        if (blockedUser.getDeletionDate() != null) {
            throw new DeletionBlockedUserException();
        }

        blockedUser.setDeletionDate(LocalDate.now(clock));
        blacklistRepository.save(blockedUser);
        log.trace("User {} removed blocked user {}", targetUserId, blockedUserId);

        notificationProducer.sendNotification(new NotificationAmqpDto(
                blockedUserId,
                NotificationType.UNBLOCKED,
                notificationSettings.getBlock()
        ));
    }

    @Deprecated
    @Override
    public void synchronizeBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        log.trace("User {} is synchronizing blocked user {}", targetUserId, blockedUserId);
        BlockedUser blockedUser = getBlockedUser(targetUserId, blockedUserId);

        String fullName = usersClient.getUserFullName(blockedUserId)
                .orElseThrow(UserNotFoundException::new);

        blockedUser.setFullName(fullName);

        blacklistRepository.save(blockedUser);
        log.trace("User {} synchronized blocked user {}", targetUserId, blockedUserId);
    }

    @Override
    public @NotNull Page<BlockedUser> getBlockedUserPage(@NotNull UUID targetUserId, int page, int size, @NotNull BlockedUserSearchDto searchDto) {
        log.trace("User {} is searching blocked users (page={}, size={}, specs={})", targetUserId, page, size, searchDto);

        Specification<BlockedUser> spec = mapper.toBlockedUserSpecification(searchDto);

        return blacklistRepository.findAll(spec, PageRequest.of(page, size));
    }

    @Override
    public boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        log.trace("User {} is checking if the user {} is blocked", targetUserId, blockedUserId);
        return blacklistRepository.existsByRelationshipAndDeletionDateNull(new Relationship(targetUserId, blockedUserId));
    }
}
