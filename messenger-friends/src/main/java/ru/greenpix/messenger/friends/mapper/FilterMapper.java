package ru.greenpix.messenger.friends.mapper;

import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
import ru.greenpix.messenger.friends.dto.FriendSearchDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Friend;

public interface FilterMapper {

    Specification<Friend> toFriendSpecification(FriendSearchDto dto);

    Specification<BlockedUser> toBlockedUserSpecification(BlockedUserSearchDto dto);

}
