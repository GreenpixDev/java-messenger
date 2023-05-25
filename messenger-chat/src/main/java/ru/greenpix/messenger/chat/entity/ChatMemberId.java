package ru.greenpix.messenger.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberId implements Serializable {

    private static final long serialVersionUID = 7737654021032193128L;
    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "member_id", nullable = false)
    private UUID userId;

}