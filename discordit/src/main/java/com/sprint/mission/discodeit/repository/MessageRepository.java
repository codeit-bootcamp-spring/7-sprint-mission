package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageRepository {
    void save(Message message);
    List<Message> findAll();
    List<Message> findBySender(User user);
    List<Message> findByReceiver(Receivable receiver);
    List<Message> findBySenderAndReceiver(User user, Receivable receiver);

    // 더미 데이터 세팅용
    Message findLast();

    Message findLast(Receivable receivable);

    void deleteAllByReceiver(Receivable receiver);

    void delete(Message message);
}
