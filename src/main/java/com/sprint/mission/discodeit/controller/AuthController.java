package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.AuthApi;
import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody AuthLoginRequestDto authLoginRequestDto) { // TODO: @Valid
        log.debug("로그인 요청 username={} ", authLoginRequestDto.username());
        UserResponseDto responseDto = authService.login(authLoginRequestDto);
        log.debug("로그인 성공 id={} username={}", responseDto.id(), responseDto.username());
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(null);
    }
}
