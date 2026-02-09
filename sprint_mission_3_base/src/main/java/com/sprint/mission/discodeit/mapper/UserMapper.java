package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserStatusMapper.class})
public interface UserMapper {

    @Mapping(target = "username", source = "username", qualifiedByName = "nullToEmpty")
    @Mapping(target = "profileUrl", ignore = true)
    UserDto toDto(User user);

    @Named("nullToEmpty")
    default String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
