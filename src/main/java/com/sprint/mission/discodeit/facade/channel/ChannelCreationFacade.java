package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChannelCreationFacade {
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

    //공개 채널 추가
    public Channel createPublicChannel(ChannelCreateReq req){
        return channelService.create(req);
    }

    //비밀 채널 추가
    public Channel createPrivateChannel(ChannelCreateSecReq req){
        Channel channel = channelService.create(req);
        req.users().forEach(userId -> readStatusService.create(
                            new ReadStatus(userId, channel.getId())));
        return channel;
    }
}
