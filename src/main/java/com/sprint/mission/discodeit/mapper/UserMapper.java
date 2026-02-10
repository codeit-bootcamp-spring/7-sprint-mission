package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final SessionRegistry sessionRegistry;

    public UserDto toDto(User user) {

        BinaryContentDto profile = (null != user.getProfile()) ? binaryContentMapper.toDto(user.getProfile()) :  null;

        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .profile(profile)
            .online(isOnline(user))
            .role(user.getRole())
            .build();
    }

    private boolean isOnline(User user) {
        List<SessionInformation> sessionInformations = sessionRegistry.getAllPrincipals()
            .stream()
            .filter(DiscodeitUserDetails.class::isInstance)
            .map(DiscodeitUserDetails.class::cast)
            .filter(userDetails -> userDetails.getUser().id().equals(user.getId()))
            .flatMap(userDetails -> sessionRegistry.getAllSessions(userDetails, false).stream())
            .toList();

        return !sessionInformations.isEmpty();
    }
}
