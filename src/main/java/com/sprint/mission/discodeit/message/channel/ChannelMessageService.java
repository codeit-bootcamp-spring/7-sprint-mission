package com.sprint.mission.discodeit.message.channel;

import com.sprint.mission.discodeit.common.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface ChannelMessageService extends BaseService<ChannelMessage, UUID> {

    /**
     * 특정 채널에 새로운 메시지를 전송합니다.
     *
     * @param channelId  메시지를 보낼 채널의 ID
     * @param senderId   메시지를 보내는 사용자의 ID
     * @param message    전송할 메시지 내용
     * @return 전송 및 저장된 ChannelMessage 객체
     */
    ChannelMessage sendMessage(UUID channelId, UUID senderId, String message);

    /**
     * 특정 채널의 모든 메시지를 시간 순으로 조회합니다.
     *
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 메시지 목록
     */
    List<ChannelMessage> getMessagesByChannel(UUID channelId);
}