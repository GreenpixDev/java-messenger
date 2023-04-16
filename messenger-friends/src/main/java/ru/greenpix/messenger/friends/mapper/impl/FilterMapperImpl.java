package ru.greenpix.messenger.friends.mapper.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.common.util.SpecificationBuilder;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
import ru.greenpix.messenger.friends.dto.FriendSearchDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.BlockedUser_;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Friend_;
import ru.greenpix.messenger.friends.mapper.FilterMapper;

public class FilterMapperImpl implements FilterMapper {

    @Override
    public @NotNull Specification<Friend> toFriendSpecification(FriendSearchDto dto) {
        return new SpecificationBuilder<Friend>()
                .equal(Friend_.additionDate, dto.getAdditionDate())
                .equal(Friend_.fullName, dto.getFullName())
                .build();
    }

    @Override
    public @NotNull Specification<BlockedUser> toBlockedUserSpecification(BlockedUserSearchDto dto) {
        return new SpecificationBuilder<BlockedUser>()
                .equal(BlockedUser_.additionDate, dto.getAdditionDate())
                .equal(BlockedUser_.fullName, dto.getFullName())
                .build();
    }
}
