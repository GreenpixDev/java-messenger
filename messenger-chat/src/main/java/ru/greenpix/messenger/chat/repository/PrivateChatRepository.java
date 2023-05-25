package ru.greenpix.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.greenpix.messenger.chat.entity.PrivateChat;

import java.util.Optional;
import java.util.UUID;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {

    @Query("select c from PrivateChat c where " +
            ":senderId in (select m from c.memberIds m) and" +
            ":receiverId in (select m from c.memberIds m)")
    Optional<PrivateChat> findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);

}