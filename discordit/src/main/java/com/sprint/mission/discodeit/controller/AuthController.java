package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.auth.response.JwtDto;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import static com.sprint.mission.discodeit.common.config.jwt.JwtProvider.REFRESH_TOKEN_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final BasicAuthService basicAuthService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청 : {}", tokenValue);
        return ResponseEntity.status(HttpStatus.valueOf(203)).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role")
    public UserDto updateRole(@Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return userService.updateRole(roleUpdateRequest.userId(), roleUpdateRequest.newRole());
    }

    @PostMapping("/refresh")
    public JwtDto refresh(@CookieValue(name = REFRESH_TOKEN_NAME, required = false) String refreshToken) {
        return basicAuthService.refresh(refreshToken);
    }
}
