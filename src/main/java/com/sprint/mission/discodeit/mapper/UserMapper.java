package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserResponseDto toDto(User user) {
    BinaryContent profile = user.getProfile();
    return new UserResponseDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        profile != null ? binaryContentMapper.toDto(user.getProfile()) : null,
        user.getStatus().isOnline(),
        user.getRole()
    );
  }
}
