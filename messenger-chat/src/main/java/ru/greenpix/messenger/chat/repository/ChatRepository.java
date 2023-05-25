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

    /**
     * Запрос на поиск чатов, в которых состоит пользователь,
     * в порядке убывания отправки последнего сообщения в чате.
     * <br><br>
     * Поддерживает фильтрацию по названию чата, в том числе по названию {@link ru.greenpix.messenger.chat.entity.PrivateChat},
     * у которых название вычисляется исходя из имени собеседника.
     * <br><br>
     * Результатом является список чатов с закрепленными за ними последними сообщениями и их названиями.
     * <br><br>
     * @param userId идентификатор пользователя
     * @param nameFilter фильтр по названию чата
     * @param pageable данные о странице
     * @return Страница из tuple'ов с чатом, последним сообщением и названием чата
     */
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

    /**
     * Запрос на проверку того, что пользователь состоит в каком-то чате
     * @param chatId идентификатор чата
     * @param memberId идентификатор пользователя
     * @return true, если пользователь memberId состоит в чате chatId
     */
    @Query("select case when (count(c) > 0) then true else false end from Chat c join c.members m where c.id = :chatId and m.id.userId = :memberId")
    boolean existsIdAndMember(UUID chatId, UUID memberId);

    /**
     * Запрос на получение конкретного чата при условии, что пользователь в нём состоит
     * @param chatId идентификатор чата
     * @param memberId идентификатор пользователя
     * @return чат chatId, если пользователь memberId в нем состоит
     */
    @Query("select distinct c from Chat c join c.members m where c.id = :chatId and m.id.userId = :memberId")
    Optional<Chat> findIdAndMember(UUID chatId, UUID memberId);

}