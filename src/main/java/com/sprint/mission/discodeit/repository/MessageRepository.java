package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>, MessageQueryRepository {
    void deleteById(UUID id);

    @Query("select m from Message m" +
            " join fetch m.user" +
            " where m.id=:messageId")
    Optional<Message> findByIdWithUser(@Param("messageId") UUID messageId);
}
