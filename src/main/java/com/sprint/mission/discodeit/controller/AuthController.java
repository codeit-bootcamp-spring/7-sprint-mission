package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swaggerDocs.AuthDoc;
import com.sprint.mission.discodeit.entity.dto.AuthServiceDto;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthDoc {
    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<Res_UserLogin> login(
        @RequestBody AuthServiceDto authServiceDto) {
       //💎로그인
        Res_UserLogin resUserLogin = authService.login(authServiceDto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resUserLogin);
    }
}
