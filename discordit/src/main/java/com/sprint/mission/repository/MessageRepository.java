package com.sprint.mission.repository;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;

import java.util.List;

public interface MessageRepository {
    void save(Message<Receivable> message);
    List<Message<Receivable>> findBySender(User user);
    <T extends Receivable> List<Message<T>> findByReceiver(T receiver);
    <T extends Receivable> List<Message<T>> findBySenderAndReceiver(User user, T receiver);
}
