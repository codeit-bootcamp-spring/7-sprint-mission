package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(CreateMessageDto createMessageDto);

    Message getMessage(UUID messageId);

    List<Message> getAllMessages();

    List<Message> getAllMessageByChannelId(UUID channelID);

    void updateMessage(UpdateMessageDto updateMessageDto);

    void deleteMessage(UUID messageId);
}
