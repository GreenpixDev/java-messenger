package ru.greenpix.messenger.chat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Сущность приватного сообщения
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("private")
public class PrivateChat extends Chat {
}