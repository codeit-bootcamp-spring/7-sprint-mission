package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
//    void save(Message message);
//
//    void remove(UUID messageId);
//
//    Optional<Message> findById(UUID messageId);
//
//    List<Message> findAll();
}
