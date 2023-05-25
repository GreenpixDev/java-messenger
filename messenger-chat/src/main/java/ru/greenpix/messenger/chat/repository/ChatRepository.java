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

    @Query("select " +
            "case when (g.name is null) " +
            "   then cm.memberName " +
            "   else g.name " +
            "end as name, c as chat, mes as message from Chat c " +
            "join c.messages mes " +
            "join GroupChat g on g = c " +
            "join ChatMember cm on cm.id.chat = c " +
            "where mes.creationTimestamp = (" +
            "   select max(x.creationTimestamp) from Message x where x.chat = c" +
            ") and (" +
            "   g.name is null and cm.id.userId <> :userId " +
            "   or " +
            "   g.name is not null and cm.id.userId = :userId" +
            ") and (" +
            "   c in (select cm2.id.chat from ChatMember cm2 where cm2.id.userId = :userId)" +
            ") and (" +
            "   lower(g.name) LIKE lower(:nameFilter) or lower(cm.memberName) LIKE lower(:nameFilter)" +
            ") order by mes.creationTimestamp desc")
    Page<Tuple> findAllWithLastMessage(UUID userId, String nameFilter, Pageable pageable);

    @Query("select case when (count(c) > 0) then true else false end from Chat c join c.members m where c.id = :chatId and m.id.userId = :memberId")
    boolean existsIdAndMember(UUID chatId, UUID memberId);

    @Query("select distinct c from Chat c join c.members m where c.id = :chatId and m.id.userId = :memberId")
    Optional<Chat> findIdAndMember(UUID chatId, UUID memberId);

}