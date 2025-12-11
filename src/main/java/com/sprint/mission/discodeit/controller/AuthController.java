package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import com.sprint.mission.discodeit.swaggerDocs.AuthDoc;
import com.sprint.mission.discodeit.mapper.dto.LoginRequest;
import com.sprint.mission.discodeit.service.basic.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //👍 @controller + @responsebody
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthDoc {
    private final InterfaceAuthService authService;

//👍- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                  : @RequestBody
//👍- http://localhost:8000/board?page=1&listSize=10  : @RequestParam
//👍- http://localhost:8000/board/1                   : @PathVariable

    @PostMapping(value = "/login")
    public ResponseEntity<UserDto> login(
        @Valid @RequestBody LoginRequest loginRequest) {
       //💎로그인
        UserDto userDto
            = authService.login(loginRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userDto);
    }
}
