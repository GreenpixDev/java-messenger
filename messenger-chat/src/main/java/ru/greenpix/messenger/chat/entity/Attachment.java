package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

/**
 * Сущность вложения
 */
@Getter
@Setter
@Entity
@Table(name = "attachment")
public class Attachment {

    /**
     * Идентификатор вложения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Идентификатор файла в файловом хранилище
     */
    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    /**
     * Название файла
     */
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * Размер файла
     */
    @Column(name = "file_size", nullable = false)
    private long fileSize;

    /**
     * Сообщение, которому принадлежит данное вложение
     */
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Attachment that = (Attachment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}