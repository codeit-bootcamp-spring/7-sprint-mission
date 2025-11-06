package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChannelCreationFacade {
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;
    private final ChannelMapper channelMapper;

    //공개 채널 추가
    public Channel createPublicChannel(@NonNull UUID managerId, @NonNull ChannelCreateReq req){
        return channelService.create(managerId, req);
    }

    //비밀 채널 추가
    public Channel createPrivateChannel(@NonNull UUID managerId, @NonNull ChannelCreateSecReq req){
        Channel channel = channelService.create(managerId, req);
        req.userIds().forEach(userId -> readStatusService.create(
                            ReadStatus.create(userId, channel.getId())
        ));
        return channel;
    }
}
