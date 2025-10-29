package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.request.SendMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.Receivable;
import com.sprint.mission.discodeit.entity.base.User;

import java.util.List;

public interface MessageService {
    void sendMessage(SendMessageDto dto);

    List<MessageResponse> getBySenderAndReceiver(User sender, Receivable receiver);
    List<MessageResponse> getBySender(User sender);
    List<MessageResponse> getByReceiver(Receivable receiver);

    void delete(Message message);
    /**
     * 테스트용 임시 메서드: 마지막으로 전송된 메시지를 반환합니다.
     */
    Message getLastMessage();
}
