package com.sprint.mission.discodeit.message.channel;

import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.message.channel.dto.ChannelMSGRequestDTO;
import com.sprint.mission.discodeit.message.channel.dto.ChannelMSGResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ChannelMessageService extends BaseService<ChannelMessage, UUID> {

    ChannelMSGResponseDTO sendMessage(ChannelMSGRequestDTO requestDTO);

    /**
     * 특정 채널의 모든 메시지를 시간 순으로 조회합니다.
     *
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 메시지 목록
     */
    List<ChannelMSGResponseDTO> getMessagesByChannel(UUID channelId);

    void deleteAllBySenderId(UUID senderId);

    int countNotReadChannelMessage(UUID channelId, UUID authorId);
}