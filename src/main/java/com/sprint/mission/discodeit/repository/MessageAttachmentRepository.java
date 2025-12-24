package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, UUID> {
    @Query("select ma from MessageAttachment ma" +
            " join fetch ma.attachment" +
            " where ma.message.id = :messageId")
    List<MessageAttachment> findAllWithBinaryContentByMessageId(
            @Param("messageId") UUID messageId);

    @Query("select ma from MessageAttachment ma" +
            " join fetch ma.message" +
            " where ma.message.id in :messageIds")
    List<MessageAttachment> findAllWithBinaryContentByMessageIds(
            @Param("messageIds") List<UUID> messageIds);

    void deleteByMessageId(UUID messageId);
}
