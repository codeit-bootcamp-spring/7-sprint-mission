package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOverviewFacade {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    //유저 전체 조회
    public List<UserSimpleInfoRes> findAll(){
        return userService.findAll().stream()
                .map(this::mapToSimpleInfo).toList();
    }

    //변환 메소드
    private UserSimpleInfoRes mapToSimpleInfo(User user) {
        byte[] profileData = binaryContentService.findById(user.getProfileId()).getData();
        boolean online = userStatusService.findByUserId(user.getId()).IsOnline();
        return UserSimpleInfoRes.from(user, profileData, online);
    }
}
