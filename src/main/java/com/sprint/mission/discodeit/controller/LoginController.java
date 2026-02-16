package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.service.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        return new ResponseEntity<>(authService.checkLoginUser(dto), HttpStatus.OK);
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken){
        String token = csrfToken.getToken();
        log.debug("csrfToken : {}", token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateUserRole(
            @Valid @RequestBody UserRoleUpdateRequestDto requestDto
            ){

        UserDto userDto = authService.updateUserRole(requestDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ){

        JwtDto jwtDto = authService.refreshToken(request,response);
        return new ResponseEntity<>(jwtDto,HttpStatus.OK);

    }
}
