package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.AuthDocs;
import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtDto;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtInformation;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtProvider;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthDocs {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> csrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        // 미션 9 요구사항: 203
        // PR 리뷰 피드백: 204
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/role")
    public ResponseEntity<UserResponseDto> updateUserRole(@RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
        log.debug("권한 수정 요청");
        UserResponseDto userResponseDto = authService.updateRoleForAdmin(userRoleUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> replaceAccessToken(@CookieValue("REFRESH_TOKEN") String refreshToken, HttpServletResponse response) {
        JwtInformation jwtInformation = authService.reIssuerAccessToken(refreshToken);
        String refresh = jwtInformation.getRefreshToken();

        Cookie refreshCookie = new Cookie(JwtProvider.REFRESH_COOKIE_NAME, refresh);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) (jwtProvider.getRefreshTokenExpirationMills() / 1000));
        response.addCookie(refreshCookie);

        JwtDto jwtDto = new JwtDto(
                jwtInformation.getUserDto(),
                jwtInformation.getAccessToken()
        );

        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }

}
