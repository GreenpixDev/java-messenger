package ru.greenpix.messenger.friends.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "friend")
public class Friend {

    /**
     * ID целевого пользователя и ID друга
     */
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "externalUserId", column = @Column(name = "friend_user_id"))
    })
    private Relationship relationship;

    /**
     * ФИО друга
     */
    @Column(name = "friend_full_name", nullable = false)
    private String fullName;

    /**
     * Дата начала дружбы
     */
    @Column(name = "addition_date", nullable = false)
    private LocalDate additionDate;

    /**
     * Дата конца дружбы
     */
    @Column(name = "deletion_date")
    private LocalDate deletionDate;

}