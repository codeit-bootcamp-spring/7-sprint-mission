package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // 한 채널의 모든 대화 목록
    @EntityGraph(attributePaths = "author") // author를 한번에 받아옴
    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);
}
