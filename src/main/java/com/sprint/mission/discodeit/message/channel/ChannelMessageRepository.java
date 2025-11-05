package com.sprint.mission.discodeit.message.channel;

import com.sprint.mission.discodeit.common.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface ChannelMessageRepository extends BaseRepository<ChannelMessage, UUID> {

    /**
     * 특정 채널에 속한 모든 메시지를 조회합니다. (삭제되지 않은 메시지만)
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 모든 메시지 목록
     */
    List<ChannelMessage> findAllByChannelId(UUID channelId);

    /**
     * 특정 유저가 보낸 모든 채널 메시지를 조회합니다. (삭제되지 않은 메시지만)
     * @param senderId 조회할 유저의 ID
     * @return 해당 유저의 모든 메시지 목록
     */
    List<ChannelMessage> findAllBySenderId(UUID senderId);
}