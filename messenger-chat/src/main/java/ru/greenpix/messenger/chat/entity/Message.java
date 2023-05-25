package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Сущность сообщения
 */
@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {

    /**
     * Идентификатор сообщения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Идентификатор отправителя
     */
    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    /**
     * Текст сообщения
     */
    @Column(name = "text", nullable = false, length = 500)
    private String text;

    /**
     * Дата отправки сообщения
     */
    @Column(name = "creation_timestamp", nullable = false)
    private LocalDateTime creationTimestamp;

    /**
     * Чат, которому принадлежит данное сообщений
     */
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    /**
     * Множество приложений сообщения
     */
    @OneToMany(
            mappedBy = "message",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private Set<Attachment> attachments = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return getId() != null && Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}