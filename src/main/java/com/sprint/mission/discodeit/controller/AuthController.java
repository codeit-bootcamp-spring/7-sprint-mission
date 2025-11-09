package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.util.ApiResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<UserResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UserResponseDto userResponseDto = authService.login(loginRequestDto);
        ApiResponse<UserResponseDto> responseBody = ApiResponse.success(userResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
