package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // 한 채널의 모든 대화 목록
    List<Message> findAllByChannelIdOrderByCreatedAtDesc(UUID channelId);

    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);
}
