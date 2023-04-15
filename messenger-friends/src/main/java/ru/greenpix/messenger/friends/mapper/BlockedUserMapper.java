package ru.greenpix.messenger.friends.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.friends.dto.BlockedUserDetailsDto;
import ru.greenpix.messenger.friends.dto.BlockedUserDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BlockedUserMapper {
    @Mapping(source = "relationship.externalUserId", target = "blockedUserId")
    @Mapping(source = "relationship.targetUserId", target = "targetUserId")
    BlockedUserDetailsDto toDetailsDto(BlockedUser blockedUser);

    @Mapping(source = "relationship.externalUserId", target = "blockedUserId")
    BlockedUserDto toDto(BlockedUser blockedUser);
}