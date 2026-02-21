package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final JwtRegistry jwtRegistry;

    public UserDto toDto(User user) {

        BinaryContentDto profile = (null != user.getProfile()) ? binaryContentMapper.toDto(user.getProfile()) :  null;

        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .profile(profile)
            .online(jwtRegistry.hasActiveJwtInformationByUserId(user.getId()))
            .role(user.getRole())
            .build();
    }
}
