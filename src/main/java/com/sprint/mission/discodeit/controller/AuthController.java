package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.auth.request.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.dto.ApiResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        User user = authService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
