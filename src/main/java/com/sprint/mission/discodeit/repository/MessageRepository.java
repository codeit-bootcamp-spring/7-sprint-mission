package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Message save(Message message);

    Optional<Message> findById(UUID id);

    List<Message> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);

    List<Message> findAllByChannelId(UUID channelId);

    void deleteAllByChannelId(UUID channelId);

    //채널아이디맞게 생성시간기준내림차로
    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

    Slice<Message> findAllByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);


}
