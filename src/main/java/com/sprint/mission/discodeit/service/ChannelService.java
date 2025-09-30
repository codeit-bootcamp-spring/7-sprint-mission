package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UpdatedChannelDTO;

import java.util.UUID;

public interface ChannelService {

    //채널 생성
    void addChannel(Channel channel);

    //채널 삭제
    void removeChannel(Channel channel);

    //채널 가져오기
    Channel getChannel(UUID id);

    //채널 수정
    void updateChannel(UUID channelId, UpdatedChannelDTO updatedChannelDTO);



}
