package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.GetMessageDto;
import com.sprint.mission.discodeit.dto.message.request.SendMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    void send(SendMessageDto dto);
    List<MessageResponse> get(GetMessageDto dto);
    void delete(Message message);
    /**
     * 테스트용 임시 메서드: 마지막으로 전송된 메시지를 반환합니다.
     */
    Message getLastMessage();
}
