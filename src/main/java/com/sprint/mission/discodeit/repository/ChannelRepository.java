package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelRepository {
    Channel createChannel(User ownerUser, String channelName);  // 생성
//    Channel getChannel(Channel channel);                        // 읽기
//    Channel[] getAllChannels();                                 // 모두 읽기
    void updateChannelName(Channel channel, String name);
    void updateChannelType(Channel channel, ChannelType channelType);// 수정
    void deleteChannel(UUID uuid);                              // 삭제
}
