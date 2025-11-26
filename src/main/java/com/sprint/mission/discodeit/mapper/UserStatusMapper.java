package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);

  @Mapping(target = "userId", source = "user.id")
  UserStatusResponseDto toResponseDto(UserStatus userStatus);
}
