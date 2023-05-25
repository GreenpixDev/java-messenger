package ru.greenpix.messenger.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.greenpix.messenger.chat.entity.Chat;

import javax.persistence.Tuple;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID>, JpaSpecificationExecutor<Chat> {

    /*@Query("select c, x from (\n" +
            "   select m, rank() over (\n" +
            "       partition by m.chat order by m.creationTimestamp desc\n" +
            "   ) as rank from Message m order by m.creationTimestamp desc\n" +
            ") x join Chat c on c = x.chat where x.rank = 1")*/
    @Query("select c as chat, m as message from Chat c join c.messages m where m.creationTimestamp = (" +
            "   select max(x.creationTimestamp) from Message x where x.chat = c" +
            ") order by m.creationTimestamp desc")
    Page<Tuple> findAllWithLastMessage(Pageable pageable);

    @Query("select case when (count(c) > 0) then true else false end from Chat c join c.memberIds m where c.id = :chatId and m = :memberId")
    boolean existsIdAndMember(UUID chatId, UUID memberId);

    @Query("select distinct c from Chat c join c.memberIds m where c.id = :chatId and m = :memberId")
    Optional<Chat> findIdAndMember(UUID chatId, UUID memberId);

}