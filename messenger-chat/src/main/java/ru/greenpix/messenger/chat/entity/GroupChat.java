package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.UUID;

/**
 * Сущность группового чата
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("group")
public class GroupChat extends Chat {

    /**
     * Идентификатор пользователя, который является администратором чата,
     * т.к. создал чат
     */
    @Column(name = "admin_user_id")
    private UUID adminUserId;

    /**
     * Название группового чата
     */
    @Column(name = "name")
    private String name;

    /**
     * Идентификатор аватарки чата в файловом хранилище
     */
    @Column(name = "avatar_id")
    private UUID avatarId;

}