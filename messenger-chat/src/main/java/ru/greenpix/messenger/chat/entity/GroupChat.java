package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@DiscriminatorValue("group")
public class GroupChat extends Chat {
    @Column(name = "admin_user_id")
    private UUID adminUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "avatar_id")
    private UUID avatarId;

    @Column(name = "creation_timestamp")
    private LocalDateTime creation_timestamp;

}