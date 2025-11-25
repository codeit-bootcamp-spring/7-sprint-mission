package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestParam String emailOrUsername,
            @RequestParam String password
    ) {

        User user = authService.login(emailOrUsername, password);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
