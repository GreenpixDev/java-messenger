package ru.greenpix.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.chat.entity.ChatMember;
import ru.greenpix.messenger.chat.entity.ChatMemberId;

import java.util.UUID;

public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId> {

    /**
     * Запрос на обновление имени и ID аватара в чатах у пользователя
     * @param id идентификатор пользователя
     * @param name новое имя пользователя
     * @param avatarId новый ID аватара пользователя
     */
    @Transactional
    @Modifying
    @Query("update ChatMember m set m.memberName = :name, m.memberAvatarId = :avatarId where m.id.userId = :id")
    void updateNameAndAvatarId(UUID id, String name, UUID avatarId);

}