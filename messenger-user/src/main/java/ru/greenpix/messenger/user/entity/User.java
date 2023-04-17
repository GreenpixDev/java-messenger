package ru.greenpix.messenger.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Дата регистрации
     */
    @Column(name = "registration_timestamp")
    private LocalDateTime registrationTimestamp;

    /**
     * Логин (уникальный)
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Почта (уникальная)
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Пароль (не в исходном виде)
     */
    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    /**
     * Фамилия, имя, отчество
     */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * Дата рождения
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Телефон
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Город
     */
    @Column(name = "city")
    private String city;

    /**
     * Город
     */
    @Column(name = "avatar_id")
    private UUID avatarId;

}