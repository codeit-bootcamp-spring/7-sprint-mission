package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.request.auth.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal DiscodeitUserDetails principal) {
        UUID userId = principal.getUserDto().id();
        return ResponseEntity.ok(userService.getMe(userId));
    }

    @PutMapping("/role")
    public ResponseEntity<UserResponseDto> role(@Valid @RequestBody UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateRole(userRoleUpdateRequestDto));
    }
}
