package ru.greenpix.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.greenpix.messenger.chat.entity.Chat;

import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID>, JpaSpecificationExecutor<Chat> {

    @Query("select case when (count(c) > 0) then true else false end from Chat c join c.memberIds m where c.id = :chatId and m = :memberId")
    boolean existsIdAndMember(UUID chatId, UUID memberId);

    @Query("select distinct c from Chat c join c.memberIds m where c.id = :chatId and m = :memberId")
    Optional<Chat> findIdAndMember(UUID chatId, UUID memberId);

}