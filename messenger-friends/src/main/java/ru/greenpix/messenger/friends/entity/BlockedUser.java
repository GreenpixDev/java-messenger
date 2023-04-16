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

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "externalUserId", column = @Column(name = "blocked_user_id"))
    })
    private Relationship relationship;

    @Column(name = "blocked_user_full_name", nullable = false)
    private String fullName;

    @Column(name = "addition_date", nullable = false)
    private LocalDate additionDate;

    @Column(name = "deletion_date")
    private LocalDate deletionDate;

}