package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    public Message createMessage(MessageCreateRequestDto messageCreateRequestDto);
    public Message readMessage(Message message);
    public List<Message> readAllMessage();
    public <T> void updateMessage(MessageUpdateRequestDto<T> messageUpdateRequestDto);
    public List<Message> readUpdatedMessage();
    public List<Message> findallByChannelId(UUID channelId);
    public void deleteMessage(UUID messageId);

}
