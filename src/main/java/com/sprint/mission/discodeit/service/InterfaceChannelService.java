package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.*;

import java.util.List;
import java.util.UUID;

public interface InterfaceChannelService extends BaseInterfaceService {
    Res_Channel createPrivate(Dto_CreateChannelPrivate dtoCreateChannel);
    Res_Channel createPublic(Dto_CreateChannelPublic dtoCreateChannel);   // 생성
    Res_ChannelFind find(Dto_Channel dtoChannel);                        // 읽기
    List<Res_ChannelFind> findAllByUserId(UUID userID);                                 // 모두 읽기
//    void updateChannelName(Channel channel, String name);
//    void updateChannelType(Channel channel, ChannelType channelType);// 수정
    void update(Dto_ChannelUpdate dtoChannelUpdate);
    void delete(UUID uuid);                              // 삭제
}
