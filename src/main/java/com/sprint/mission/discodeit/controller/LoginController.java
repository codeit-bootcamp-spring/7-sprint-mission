package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;
    // [등록]
    @RequestMapping(method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest req) {
        User login = authService.login(req);
        return LoginResponse.from(login,true);
    }

}
