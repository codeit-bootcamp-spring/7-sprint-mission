package com.sprint.mission.service;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageService {
    void sendMessage(User sender, Receivable receiver, String message);

    <T extends Receivable> List<Message<T>> getBySenderAndReceiver(User sender, T receiver);
    List<Message<Receivable>> getBySender(User sender);
    <T extends Receivable> List<Message<T>> getByReceiver(T receiver);

    /**
     * 테스트용 임시 메서드: 마지막으로 전송된 메시지를 반환합니다.
     */
    Message<Receivable> getLastMessage();
}
