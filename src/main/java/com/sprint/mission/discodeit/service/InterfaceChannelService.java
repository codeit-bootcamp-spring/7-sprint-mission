package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.dto_Neo.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.dto.dto_Neo.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.dto_Neo.PublicChannelCreateRequest;
import java.util.List;
import java.util.UUID;

public interface InterfaceChannelService {
    ChannelDto createPrivate(PrivateChannelCreateRequest dtoCreateChannel);
    ChannelDto createPublic(PublicChannelCreateRequest dtoCreateChannel);   // 생성
    List<ChannelDto> findAllByUserId(UUID userID);
    ChannelDto update(UUID channelId, ChannelDto_Update channelDtoUpdate);
    void delete(UUID uuid);                              // 삭제
}
