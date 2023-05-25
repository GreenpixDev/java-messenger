package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "chat_member_ids")
public class ChatMember {

    @EmbeddedId
    private ChatMemberId id;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_avatar_id")
    private UUID memberAvatarId;

}