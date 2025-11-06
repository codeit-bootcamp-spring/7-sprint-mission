package com.sprint.mission.discodeit.facade.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.factory.ReadStatusFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusCreateFacade {
    private final ReadStatusService readStatusService;
    private final ChannelService channelService;
    private final UserService userService;

    //유저 채널 읽음 상태 추가
    public ReadStatus create(@NonNull ReadStatusCreateReq req){
        if(channelService.findById(req.channelId()) == null){
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        if(userService.findById(req.userId()) == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return readStatusService.create(ReadStatusFactory.create(req));
    }
}
