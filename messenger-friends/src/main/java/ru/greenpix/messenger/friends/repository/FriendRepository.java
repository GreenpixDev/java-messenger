package ru.greenpix.messenger.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Relationship;

import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, Relationship>, JpaSpecificationExecutor<Friend> {

    /**
     * Метод поиска всех друзей, у которых отсутствует дата удаления по
     * ID целевого пользователя и искомого пользователя, а также по like выражению с ФИО искомого пользователя.
     * @param pageable данные о пагинации
     * @param relationshipTargetUserId ID целевого и искомого пользователя
     * @param fullNameExpression like выражение с ФИО искомого пользователя
     * @return страница друзей
     */
    Page<Friend> findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
            Pageable pageable,
            UUID relationshipTargetUserId,
            String fullNameExpression
    );

    @Transactional
    @Modifying
    @Query("update Friend f set f.fullName = :fullName where f.relationship.externalUserId = :id")
    void updateFullName(UUID id, String fullName);

}