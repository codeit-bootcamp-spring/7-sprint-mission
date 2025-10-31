package com.sprint.mission.discodeit.facade.auth;

import com.sprint.mission.discodeit.dto.user.request.UserLoginReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthFacade {
    private final UserService userService;
    private final UserStatusService userStatusService;

    //로그인
    public boolean login(UserLoginReq req){
        User user = userService.findByNickname(req.nickname());
        if(user == null){
            throw new RuntimeException("Invalid nickname");
        }
        if(!user.getPassword().equals(req.password())){
            throw new RuntimeException("Invalid password");
        }
        userStatusService.updateOnlineAt(user.getId());
        return true;
    }

    //로그아웃
    public boolean logout(UUID userId){
        userStatusService.updateOfflineAt(userId);
        return true;
    }
}
