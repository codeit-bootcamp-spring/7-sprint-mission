package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
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
public class LoginController implements LoginControllerDocs {

    private final AuthService authService;
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Override
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        return new ResponseEntity<>(authService.checkLoginUser(dto), HttpStatus.OK);
    }
}
