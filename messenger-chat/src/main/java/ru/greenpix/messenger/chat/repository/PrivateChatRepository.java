package ru.greenpix.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.greenpix.messenger.chat.entity.PrivateChat;

import java.util.Optional;
import java.util.UUID;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {

    /**
     * Запрос на поиск приватного чата по отправителю и получателю
     * (или другими словами по двум участникам приватного чата)
     * @param senderId идентификатор пользователя-отправителя (одного участника)
     * @param receiverId идентификатор пользователя-получателя (другого участника)
     * @return приватный чат, если такой найден
     */
    @Query("select c from PrivateChat c where " +
            ":senderId in (select m.id.userId from c.members m) and" +
            ":receiverId in (select m.id.userId from c.members m)")
    Optional<PrivateChat> findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);

}