package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, UUID> {
    @Query("select ma from MessageAttachment ma" +
            " join fetch ma.attachment" +
            " where ma.message.id = :messageId")
    List<MessageAttachment> findAllWithBinaryContentByMessageId(
            @Param("messageId") UUID messageId);

    void deleteByMessageId(UUID messageId);
}
