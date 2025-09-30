package com.sprint.mssion.discodeit.service;

import com.sprint.mssion.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    public Message create(String message, UUID channelId, UUID userId);
    public Message read(UUID messageId);
    public List<Message> readAll();
    public Message update(UUID messageId, String message);
    public void delete(UUID messageId);
}
