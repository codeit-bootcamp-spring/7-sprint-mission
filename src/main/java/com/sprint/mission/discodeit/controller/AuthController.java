package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.PrintUtil;
import com.sprint.mission.discodeit.entity.dto.Dto_AuthService;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController  extends BaseController {
    private final AuthService authService;
//    권한 관리
//    [ ] 사용자는 로그인할 수 있다.
    @RequestMapping(value = "Dto_AuthService", method = RequestMethod.POST)
    public void isLogin(@RequestBody Dto_AuthService dtoAuthService) {
       //return authService.isLogin(dtoAuthService);
        PrintUtil.okMessage("userName = " + dtoAuthService.userName());
    }
}
