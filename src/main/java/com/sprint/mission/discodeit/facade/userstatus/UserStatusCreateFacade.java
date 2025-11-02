package com.sprint.mission.discodeit.facade.userstatus;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusCreateFacade {
    private final UserStatusService userStatusService;
    private final UserService userService;

    //UserState
    public UserStatus create(UserStatusCreateReq req){
        User user = userService.findById(req.userId());
        return userStatusService.create(new UserStatus(user.getId()));
    }
}
