package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "message_attachments")
public class MessageAttachments {
    @Column(name = "message_id")
    UUID messageId;

    @Column(name = "attachment_id")
    UUID attachmentId;
}
