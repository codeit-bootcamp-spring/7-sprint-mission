package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  List<Message> findAllByChannelId(UUID channelId);

  Instant getListMessageTime(UUID channelId);

  Message create(Message message);

  void update(UUID id, String content, List<BinaryContent> attachments);

  void delete(UUID id);

  Message findById(UUID id);
}
