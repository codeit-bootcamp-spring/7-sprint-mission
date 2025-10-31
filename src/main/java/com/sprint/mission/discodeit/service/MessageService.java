package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<Message> findAllByUser(UUID userId);
    List<Message> findAllByChannel(UUID channelId);
    List<Message> searchMessagesByContent(String searchText);
    Message findLastMessageByChannelId(UUID channelId);
    Message create(Message message);
    Message update(UUID id, String content);
    Message delete(UUID id);
}
