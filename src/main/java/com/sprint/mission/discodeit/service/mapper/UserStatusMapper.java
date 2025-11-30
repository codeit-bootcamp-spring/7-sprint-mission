package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.dto.response.UserStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

    @Mapping(source = "user.id",target = "userId")
    UserStatusDto toDto(UserStatus userStatus);
}
