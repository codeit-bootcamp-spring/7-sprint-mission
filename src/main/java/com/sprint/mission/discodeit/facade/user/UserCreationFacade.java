package com.sprint.mission.discodeit.facade.user;


import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.factory.UserFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCreationFacade {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    //유저 추가
    public User createUser(@NonNull UserCreateReq req) {
        UUID profileId = null;

        if (req.profileImage().data() != null) {

            BinaryContent profile = binaryContentService.create(
                    BinaryContentFactory.create(req.profileImage())
            );

            profileId = profile.getId();
        }

        User user = userService.create(UserFactory.create(req, profileId));
        userStatusService.create(UserStatus.create(user.getId()));
        return user;
    }
}
