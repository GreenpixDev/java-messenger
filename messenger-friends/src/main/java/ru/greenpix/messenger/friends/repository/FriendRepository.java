package ru.greenpix.messenger.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Relationship;

import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, Relationship> {

    Page<Friend> findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
            Pageable pageable,
            UUID relationshipTargetUserId,
            String fullNameExpression
    );

}