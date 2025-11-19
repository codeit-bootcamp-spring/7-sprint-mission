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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @RequestMapping(value ="/auth/login",method = RequestMethod.POST)
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        User user = authService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
