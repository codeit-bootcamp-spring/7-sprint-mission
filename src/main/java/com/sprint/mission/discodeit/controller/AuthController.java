package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.Dto_AuthService;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController  extends BaseController {
    private final AuthService authService;
//    권한 관리
//    [ ] 사용자는 로그인할 수 있다.
    @RequestMapping(value = "isLogin", method = RequestMethod.POST)
    public Res_UserLogin isLogin(@RequestBody Dto_AuthService dtoAuthService) {
       return authService.isLogin(dtoAuthService);
    }
}
