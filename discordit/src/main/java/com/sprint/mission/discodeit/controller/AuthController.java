package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청 : {}", tokenValue);
        return ResponseEntity.status(HttpStatus.valueOf(203)).build();
    }

    @GetMapping("/me")
    public UserDto getMe(@AuthenticationPrincipal DiscodeitUserDetails discodeitUserDetails) {
        return discodeitUserDetails.getUserDto();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role")
    public UserDto updateRole(@Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return userService.updateRole(roleUpdateRequest.userId(), roleUpdateRequest.newRole());
    }
}
