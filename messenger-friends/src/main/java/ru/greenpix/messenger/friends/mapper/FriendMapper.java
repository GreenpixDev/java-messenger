package ru.greenpix.messenger.friends.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.friends.dto.FriendDetailsDto;
import ru.greenpix.messenger.friends.dto.FriendDto;
import ru.greenpix.messenger.friends.entity.Friend;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendMapper {
    @Mapping(source = "relationship.targetUserId", target = "targetUserId")
    @Mapping(source = "relationship.externalUserId", target = "friendUserId")
    FriendDetailsDto toDetailsDto(Friend friend);

    @Mapping(source = "relationship.externalUserId", target = "friendUserId")
    FriendDto toDto(Friend friend);
}