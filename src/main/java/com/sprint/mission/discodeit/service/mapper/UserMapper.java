package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
