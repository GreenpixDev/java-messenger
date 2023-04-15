package ru.greenpix.messenger.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Relationship;

import java.util.UUID;

public interface BlacklistRepository extends JpaRepository<BlockedUser, Relationship> {

    Page<BlockedUser> findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
            Pageable pageable,
            UUID relationshipTargetUserId,
            String fullNameExpression
    );

    boolean existsByRelationshipAndDeletionDateNull(Relationship relationship);

}