package ru.greenpix.messenger.friends.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.common.exception.UserNotFoundException;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Relationship;
import ru.greenpix.messenger.friends.exception.AdditionFriendException;
import ru.greenpix.messenger.friends.exception.DeletionFriendException;
import ru.greenpix.messenger.friends.exception.FriendNotFoundException;
import ru.greenpix.messenger.friends.integration.users.client.UsersClient;
import ru.greenpix.messenger.friends.repository.FriendRepository;
import ru.greenpix.messenger.friends.service.FriendService;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final Clock clock;
    private final UsersClient usersClient;

    @Override
    public @NotNull Page<Friend> getFriendPage(
            @NotNull UUID targetUserId,
            int page,
            int size,
            @NotNull String fullNameFilter
    ) {
        return friendRepository.findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
                PageRequest.of(page, size),
                targetUserId,
                "%" + fullNameFilter + "%"
        );
    }

    @Override
    public @NotNull Friend getFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId) {
        return friendRepository.findById(new Relationship(targetUserId, friendUserId))
                .orElseThrow(FriendNotFoundException::new);
    }

    @Transactional
    @Override
    public void addFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId) {
        Relationship relationship = new Relationship(targetUserId, friendUserId);
        Friend friend = friendRepository.findById(relationship).orElse(null);

        if (friend != null) {
            if (friend.getDeletionDate() == null) {
                throw new AdditionFriendException();
            }
            friend.setDeletionDate(null);
        }
        else {
            String fullName = usersClient.getUserFullName(friendUserId)
                    .orElseThrow(UserNotFoundException::new);

            friend = new Friend();
            friend.setRelationship(relationship);
            friend.setFullName(fullName);
        }
        friend.setAdditionDate(LocalDate.now(clock));

        friendRepository.save(friend);
    }

    @Transactional
    @Override
    public void deleteFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId) {
        Friend friend = friendRepository.findById(new Relationship(targetUserId, friendUserId))
                .orElseThrow(DeletionFriendException::new);

        if (friend.getDeletionDate() != null) {
            throw new DeletionFriendException();
        }

        friend.setDeletionDate(LocalDate.now(clock));
        friendRepository.save(friend);
    }
}
