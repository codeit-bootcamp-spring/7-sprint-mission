package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Mapper(
        componentModel = "spring",
        uses = {BinaryContentMapper.class} // profile 매핑에 사용
)
public interface UserMapper {


    @Mapping(
            target = "online",
            expression = "java(entity.getStatus() != null && entity.getStatus().isOnline())"
    )
    UserDto toDto(User entity);
}