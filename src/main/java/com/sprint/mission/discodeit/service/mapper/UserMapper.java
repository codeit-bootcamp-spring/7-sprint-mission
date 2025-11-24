package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserEntity userEntity);

    UserEntity toUserEntity(User user);

}
