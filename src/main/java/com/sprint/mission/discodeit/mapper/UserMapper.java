package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final JwtRegistry jwtRegistry;

    public UserResponseDto toResponseDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentMapper.toResponseDto(user.getProfile()),
                jwtRegistry.hasActiveJwtInformationByUserId(user.getId()),
                user.getRole()
        );
    }
}
