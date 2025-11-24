package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.dto.Dto_CreateChannelPublic;
import com.sprint.mission.discodeit.dto.Res_Channel;
import com.sprint.mission.discodeit.dto.Res_ChannelFind;
import java.util.List;
import java.util.UUID;

public interface InterfaceChannelService {
    Res_Channel createPrivate(Dto_CreateChannelPrivate dtoCreateChannel);
    Res_Channel createPublic(Dto_CreateChannelPublic dtoCreateChannel);   // 생성
    Res_ChannelFind find(ChannelDto dtoChannel);                        // 읽기
    List<Res_ChannelFind> findAllByUserId(UUID userID);                                 // 모두 읽기
//    void updateChannelName(Channel channel, String name);
//    void updateChannelType(Channel channel, ChannelType channelType);// 수정
    Res_Channel update(UUID channelId, ChannelDto_Update channelDtoUpdate);
    void delete(UUID uuid);                              // 삭제
}
