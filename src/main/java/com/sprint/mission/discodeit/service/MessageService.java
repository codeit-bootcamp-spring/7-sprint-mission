package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    public MessageReadResponseDto createMessage(MessageCreateRequestDto messageCreateRequestDto);
    public MessageReadResponseDto readMessage(Message message);
    public List<MessageReadResponseDto> readAllMessage();
    public <T> void updateMessage(MessageUpdateRequestDto<T> messageUpdateRequestDto);
    public List<MessageReadResponseDto> readUpdatedMessage();
    public List<MessageReadResponseDto> findallByChannelId(UUID channelId);
    public void deleteMessage(UUID messageId);

    List<MessageReadResponseDto> readAllMessageByUserId(UUID userId);
    void resetMessage();
}
