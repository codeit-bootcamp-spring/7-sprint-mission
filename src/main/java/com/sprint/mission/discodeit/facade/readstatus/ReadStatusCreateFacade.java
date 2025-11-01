package com.sprint.mission.discodeit.facade.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateReq;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.factory.ReadStatusFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusCreateFacade {
    private final ReadStatusService readStatusService;
    private final ChannelService channelService;
    private final UserService userService;

    //유저 채널 읽음 상태 추가
    public ReadStatus create(ReadStatusCreateReq req){
        if(channelService.findById(req.channelId()) == null){
            throw new RuntimeException("Invalid channel id");
        }
        if(userService.findById(req.userId()) == null){
            throw new RuntimeException("Invalid user id");
        }
        return readStatusService.create(ReadStatusFactory.create(req));
    }
}
