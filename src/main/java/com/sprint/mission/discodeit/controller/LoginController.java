package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.Docs.LoginControllerDocs;
import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController implements LoginControllerDocs {

    private final AuthService authService;

    // [등록]
    @RequestMapping(path = "login",method = RequestMethod.POST)
    public ResponseEntity<User> login(@RequestBody LoginRequest req) {
        User login = authService.login(req);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(login);
    }

}
