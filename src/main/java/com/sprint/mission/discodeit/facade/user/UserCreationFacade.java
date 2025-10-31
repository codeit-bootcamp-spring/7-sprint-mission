package com.sprint.mission.discodeit.facade.user;


import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreationFacade {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    //유저 추가
    public User createUser(UserCreateReq req) {
        User user = userService.create(req);
        if(req.profileId() != null){
            BinaryContent profileImg = binaryContentService.findById(req.profileId());
            binaryContentService.create(profileImg);
            user.updateProfile(profileImg.getId());
        }
        userStatusService.create(new UserStatus(user.getId()));
        return user;
    }
}
