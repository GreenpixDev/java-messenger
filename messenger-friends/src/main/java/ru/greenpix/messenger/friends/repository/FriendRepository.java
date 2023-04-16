package ru.greenpix.messenger.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Relationship;

import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, Relationship>, JpaSpecificationExecutor<Friend> {

    Page<Friend> findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
            Pageable pageable,
            UUID relationshipTargetUserId,
            String fullNameExpression
    );

}