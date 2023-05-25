package ru.greenpix.messenger.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Relationship;

import java.util.UUID;

public interface BlacklistRepository extends JpaRepository<BlockedUser, Relationship>, JpaSpecificationExecutor<BlockedUser> {

    /**
     * Метод поиска всех заблокированных пользователей, у которых отсутствует дата удаления по
     * ID целевого пользователя и искомого пользователя, а также по like выражению с ФИО искомого пользователя.
     * @param pageable данные о пагинации
     * @param relationshipTargetUserId ID целевого и искомого пользователя
     * @param fullNameExpression like выражение с ФИО искомого пользователя
     * @return страница заблокированных пользователей
     */
    Page<BlockedUser> findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
            Pageable pageable,
            UUID relationshipTargetUserId,
            String fullNameExpression
    );

    /**
     * Метод проверки существования сущности {@link BlockedUser}, у которой отсутствует дата удаления
     * по ID целевого пользователя и искомого пользователя.
     * @param relationship ID целевого и искомого пользователя
     * @return true, если сущность {@link BlockedUser} существует
     */
    boolean existsByRelationshipAndDeletionDateNull(Relationship relationship);

    @Transactional
    @Modifying
    @Query("update BlockedUser b set b.fullName = :fullName where b.relationship.externalUserId = :id")
    void updateFullName(UUID id, String fullName);
}