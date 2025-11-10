package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    Optional<Message> find(UUID id);
    List<Message> findAll();
    List<Message> findBySender(User user);
    List<Message> findByReceiver(Receivable receiver);
    List<Message> findBySenderAndReceiver(User user, Receivable receiver);
    Optional<Message> findLast();
    Optional<Message> findLast(Receivable receivable);

    void delete(Message message);
    void deleteAllByReceiver(Receivable receiver);

    Optional<Message> update(Message message);
}
