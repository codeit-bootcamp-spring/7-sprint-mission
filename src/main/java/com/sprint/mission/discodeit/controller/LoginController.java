package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInfo(
            @AuthenticationPrincipal DiscodeitUserDetails userDetails
            )
    {
        return new ResponseEntity<>(userDetails.getUserDto(), HttpStatus.OK);
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateUserRole(
            @Valid @RequestBody UserRoleUpdateRequestDto requestDto
            ){

        UserDto userDto = authService.updateUserRole(requestDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
