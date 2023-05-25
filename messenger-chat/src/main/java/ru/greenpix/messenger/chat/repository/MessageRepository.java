package ru.greenpix.messenger.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.greenpix.messenger.chat.entity.Message;

import javax.persistence.Tuple;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>, JpaSpecificationExecutor<Message> {

    Page<Message> findAllByChatId(UUID chatId, Pageable pageable);

    /**
     * Запрос на поиск сообщений в переписках пользователя.
     * Сообщения выводятся в порядке убывания их отправки.
     * <br><br>
     * Поддерживает фильтрацию по тексту сообщения,
     * в том числе по названию вложения {@link ru.greenpix.messenger.chat.entity.Attachment}.
     * <br><br>
     * Результатом является список чатов с закрепленными за ними названиями вложений и названиями чатов.
     * <br><br>
     * @param userId идентификатор пользователя
     * @param textFilter фильтр по тексту сообщения или названию вложения
     * @param pageable данные о странице
     * @return Страница из tuple'ов с чатом, последним сообщением и названием чата
     */
    @Query("select " +
            "case when (g.name is null) " +
            "   then cm.memberName " +
            "   else g.name " +
            "end as chatName, a.fileName as attachmentName, m as message from Message m " +
            "left join m.attachments a " +
            "join m.chat c " +
            "join c.members cm " +
            "join GroupChat g on g = c " +
            "where (" +
            "   lower(m.text) like lower(:textFilter) or m in (" +
            "       select a2.message from Attachment a2" +
            "       where a2 = a and lower(a2.fileName) like lower(:textFilter)" +
            "   )" +
            ") and (" +
            "   g.name is null and cm.id.userId <> :userId " +
            "   or " +
            "   g.name is not null and cm.id.userId = :userId" +
            ") and (" +
            "   c in (select cm2.id.chat from ChatMember cm2 where cm2.id.userId = :userId)" +
            ")")
    Page<Tuple> findAllWithChatNameAndAttachmentNames(UUID userId, String textFilter, Pageable pageable);

}