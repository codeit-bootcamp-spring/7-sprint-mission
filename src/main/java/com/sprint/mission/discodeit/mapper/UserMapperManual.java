package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapperManual {

    private final BinaryContentMapperManual binaryContentMapper;
    private final SessionRegistry sessionRegistry;

    public UserResponseDto toDto(User user) {
        if (user == null) return null;
        BinaryContentResponseDto binaryContentResponseDto = Optional.ofNullable(user.getProfile()) // TODO: lazy가 나을지 eager가 나을지 혹은 feth join?
                .map(binaryContentMapper::toDto)
                .orElse(null);

        UserActiveStatus userActiveStatus = hasActiveSession(user)
                ? UserActiveStatus.ONLINE
                : UserActiveStatus.OFFLINE;

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentResponseDto,
                userActiveStatus == UserActiveStatus.ONLINE,
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public UserResponseDto toAuthDto(User user) {
        if (user == null) return null;

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                null,
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private boolean hasActiveSession(User user) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof DiscodeitUserDetails userDetails && userDetails.getUserResponseDto().id().equals(user.getId())) {
                return !sessionRegistry.getAllSessions(principal, false).isEmpty();
            }
        }
        return false;
    }

}
