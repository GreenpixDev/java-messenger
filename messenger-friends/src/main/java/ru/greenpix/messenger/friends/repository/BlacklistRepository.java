package ru.greenpix.messenger.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Relationship;

import java.util.UUID;

public interface BlacklistRepository extends JpaRepository<BlockedUser, Relationship>, JpaSpecificationExecutor<BlockedUser> {

    Page<BlockedUser> findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
            Pageable pageable,
            UUID relationshipTargetUserId,
            String fullNameExpression
    );

    boolean existsByRelationshipAndDeletionDateNull(Relationship relationship);

}