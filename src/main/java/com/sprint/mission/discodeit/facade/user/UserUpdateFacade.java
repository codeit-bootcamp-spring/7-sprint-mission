package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
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
    public void updateUser(UUID userId, UserUpdateReq req){
        User user = userService.findById(userId);
        UUID profileId = null;


        if(req.profileImage().data() != null){
            //기존 사진이 있으면 삭제
            if(user.getProfileId() != null){
                binaryContentService.delete(user.getProfileId());
            }
            BinaryContent profileImg = binaryContentService.create(
                BinaryContentFactory.create(req.profileImage())
            );
            profileId = profileImg.getId();

        }
        userService.update(userId, req);
        userService.updateProfileImage(userId, profileId);
        userStatusService.updateByUserId(userId);
    }
}
