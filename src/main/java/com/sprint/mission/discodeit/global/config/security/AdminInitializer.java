package com.sprint.mission.discodeit.global.config.security;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserService userService;
    private final AuthService authService;
    private final AdminAccountProperties adminProperties;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void adminInit() {
        UserResponseDto userResponseDto = userService.initAdmin(
                adminProperties.adminEmail(),
                adminProperties.adminName(),
                adminProperties.adminPass()
        );

        if (!userResponseDto.role().equals(Role.ADMIN)) {
            authService.updateRole(new UserRoleUpdateRequest(userResponseDto.id(), Role.ADMIN));
        }
    }
}
