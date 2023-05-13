package ru.greenpix.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.greenpix.messenger.chat.entity.PrivateChat;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {

    @Query("select c from Chat c where c.memberIds = :memberIds")
    Optional<PrivateChat> findByMemberIds(Collection<UUID> memberIds);

}