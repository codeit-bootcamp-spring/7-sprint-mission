package com.sprint.mission.discodeit.facade.userstatus;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusUpdateFacade {
    private final UserService userService;
    private final UserStatusService userStatusService;

    //update
    public UserStatusSimpleViewRes update(@NonNull UUID userId){
        userService.findById(userId);     //유저 있나 확인
        userStatusService.updateByUserId(userId);
        return UserStatusSimpleViewRes.from(userStatusService.findByUserId(userId));
    }
}
