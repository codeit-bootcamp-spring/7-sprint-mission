package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUpdateFacade {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    //유저 수정
    public User updateUser(UUID userId, UserUpdateReq req){
        User user = userService.update(userId, req);
        if(req.profileId() != null){
            BinaryContent profileImg = binaryContentService.findById(req.profileId());
        }
        userStatusService.updateOnlineAt(userId);
        return user;
    }
}
