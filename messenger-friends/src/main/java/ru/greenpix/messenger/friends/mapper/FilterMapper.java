package ru.greenpix.messenger.friends.mapper;

import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
import ru.greenpix.messenger.friends.dto.FriendSearchDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Friend;

public interface FilterMapper {

    /**
     * Метод конвертации DTO списка фильтров друзей в объект спецификации spring'а
     * @param dto список параметров сортировки
     * @return конвертированный {@link Specification} сущностей {@link Friend} для работы с репозиторием
     */
    Specification<Friend> toFriendSpecification(FriendSearchDto dto);

    /**
     * Метод конвертации DTO списка фильтров заблокированных пользователей в объект спецификации spring'а
     * @param dto список параметров сортировки
     * @return конвертированный {@link Specification} сущностей {@link BlockedUser} для работы с репозиторием
     */
    Specification<BlockedUser> toBlockedUserSpecification(BlockedUserSearchDto dto);

}
