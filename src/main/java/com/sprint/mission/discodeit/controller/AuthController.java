package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.AuthApi;
import com.sprint.mission.discodeit.dto.auth.JwtDto;
import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final UserService userService;
    private final AuthService authService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(@AuthenticationPrincipal DiscodeitUserDetails principal) {
        UUID id = principal.getUserResponseDto().id();
        UserResponseDto userResponseDto = userService.getUserById(id);
        return ResponseEntity.ok(userResponseDto);
    }

    @Override
    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(null);
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refreshAccessToken(@CookieValue("REFRESH_TOKEN") String refreshToken) {
        JwtDto jwtDto = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(jwtDto);
    }

    @Override
    @GetMapping("/session-info")
    public ResponseEntity<Map<String, Object>> getSessionInfo(HttpSession session) {
        Map<String, Object> sessionInfo = Map.of(
                "loginIP", session.getAttribute("LOGIN_IP"),
                "loginTime", session.getAttribute("LOGIN_TIME"),
                "loginUserAgent", session.getAttribute("USER_AGENT")
        );

        return ResponseEntity.ok(sessionInfo);
    }

    @Override
    @PutMapping("/role")
    public ResponseEntity<UserResponseDto> updateUserRole(RoleUpdateRequest request) {
        UserResponseDto userResponseDto = userService.updateUserRole(request);
        return ResponseEntity.ok(userResponseDto);
    }
}
