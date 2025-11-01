package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailViewFacade {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    //유저 단일 조회 : 닉네임
    public UserDetailInfoRes findByNickname(String nickname){
        User user = userService.findByNickname(nickname);
        return toDetailInfo(user);
    }
    
    //유저 단일 조회 : 이메일
    public UserDetailInfoRes findByEmail(String email){
        User user = userService.findByEmail(email);
        return toDetailInfo(user);
    }

    //변환 메소드
    private UserDetailInfoRes toDetailInfo(User user) {
        BinaryContentInfoRes profileImg = BinaryContentInfoRes.from(
                binaryContentService.findById(user.getProfileId())
        );
        UserStatus userStatus = userStatusService.findByUserId(user.getId());
        userStatus.updateOnline();
        return UserDetailInfoRes.from(user, profileImg, userStatus.isOnline());
    }
}
