package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.request.MessageDeleteRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    MessageResponse send(MessageSendRequest dto);
    List<MessageResponse> get(MessageGetRequest dto);
    void delete(MessageDeleteRequest message);
    /**
     * 테스트용 임시 메서드: 마지막으로 전송된 메시지를 반환합니다.
     */
    Message getLastMessage();

    MessageResponse editMessage(MessageEditRequest request);
}
