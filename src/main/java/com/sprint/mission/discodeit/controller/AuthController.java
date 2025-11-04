package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.request.LoginUserDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginUserDto loginUserDto){
        try {
            authService.login(loginUserDto);
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("login success");
    }
}
