package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.LoginRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    //커스텀 CsrfTokenRequestHandler가 이미 .getToken()을 하고 있기 때문에 아무 내용이 없어도 쿠키는 구워짐
    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDto userDto = userService.login(loginRequest.username(), loginRequest.password());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
