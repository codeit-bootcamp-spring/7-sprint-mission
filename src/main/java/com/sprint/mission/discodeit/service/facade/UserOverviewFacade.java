package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.response.UserSimpleInfoRes;
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
                .map(user ->
                        UserSimpleInfoRes.from(
                                user,
                                binaryContentService.findById(user.getProfileId()).getData(),
                                userStatusService.findByUserId(user.getId()).IsOnline()
                        )
                ).toList();
    }
}
