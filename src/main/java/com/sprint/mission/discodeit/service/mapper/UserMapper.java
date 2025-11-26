package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserEntity userEntity);

    UserEntity toUserEntity(User user);

    UserDto toUserDto(UserEntity userEntity);

}
