package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Сущность чата
 */
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Chat {

    /**
     * Идентификатор чата
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Дата и время создания чата
     */
    @Column(name = "creation_timestamp")
    private LocalDateTime creationTimestamp;

    /**
     * Множество участников чата
     */
    @OneToMany(mappedBy = "id.chat",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ChatMember> members = new LinkedHashSet<>();

    /**
     * Множество сообщений в чате
     */
    @OneToMany(mappedBy = "chat", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Message> messages = new LinkedHashSet<>();

}