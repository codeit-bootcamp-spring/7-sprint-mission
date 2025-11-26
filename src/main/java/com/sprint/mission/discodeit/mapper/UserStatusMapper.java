package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusDto;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface UserStatusMapper {

    UserStatusDto toDto(UserStatus entity);


}