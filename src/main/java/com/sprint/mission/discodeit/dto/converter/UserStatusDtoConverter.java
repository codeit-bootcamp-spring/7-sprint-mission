package com.sprint.mission.discodeit.dto.converter;

import com.sprint.mission.discodeit.dto.userstatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserStatusDtoConverter {

    public UserStatusResponseDto toResponseDto(UserStatus userStatus){
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastActiveAt()
        );
    }
}
