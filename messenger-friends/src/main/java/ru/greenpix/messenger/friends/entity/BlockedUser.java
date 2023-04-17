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
@Table(name = "blocked_user")
public class BlockedUser {

    /**
     * ID целевого пользователя и ID заблокированного пользователя
     */
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "externalUserId", column = @Column(name = "blocked_user_id"))
    })
    private Relationship relationship;

    /**
     * ФИО заблокированного пользователя
     */
    @Column(name = "blocked_user_full_name", nullable = false)
    private String fullName;

    /**
     * Дата добавления в черный список
     */
    @Column(name = "addition_date", nullable = false)
    private LocalDate additionDate;

    /**
     * Дата удаления из черного списка
     */
    @Column(name = "deletion_date")
    private LocalDate deletionDate;

}