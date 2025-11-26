package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.mapper.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.mapper.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.mapper.dto.Dto_CreateChannelPublic;
import java.util.List;
import java.util.UUID;

public interface InterfaceChannelService {
    ChannelDto createPrivate(Dto_CreateChannelPrivate dtoCreateChannel);
    ChannelDto createPublic(Dto_CreateChannelPublic dtoCreateChannel);   // 생성
    List<ChannelDto> findAllByUserId(UUID userID);
    ChannelDto update(UUID channelId, ChannelDto_Update channelDtoUpdate);
    void delete(UUID uuid);                              // 삭제
}
