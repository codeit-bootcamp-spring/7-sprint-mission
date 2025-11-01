package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<Message> findAllByUser(UUID userId);
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> searchMessagesByContent(String searchText);
    Message findLastMessageByChannelId(UUID channelId);
    Message create(Message message);
    Message update(UUID id, String content, List<UUID> attachmentIds);
    Message delete(UUID id);
    Message findById(UUID id);
}
