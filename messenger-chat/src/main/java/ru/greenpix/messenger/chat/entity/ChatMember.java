package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Сущность участника чата
 */
@Getter
@Setter
@Entity
@Table(name = "chat_member_ids")
public class ChatMember {

    /**
     * Идентификатор участника чата
     */
    @EmbeddedId
    private ChatMemberId id;

    /**
     * Имя участника
     */
    @Column(name = "member_name", nullable = false)
    private String memberName;

    /**
     * Идентификатор аватарки участника в файловом хранилище
     */
    @Column(name = "member_avatar_id")
    private UUID memberAvatarId;

}