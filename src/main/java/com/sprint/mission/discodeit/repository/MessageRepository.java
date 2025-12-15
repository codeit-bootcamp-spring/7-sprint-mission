package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>, MessageQueryRepository {
    void deleteById(UUID id);
}
