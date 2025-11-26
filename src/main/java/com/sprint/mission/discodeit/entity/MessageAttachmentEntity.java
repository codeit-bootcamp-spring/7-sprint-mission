package com.sprint.mission.discodeit.entity;


import jakarta.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;


@Setter
@Table(name = "message_attachments")
@Entity
public class MessageAttachmentEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private MessageEntity messageId;

    @OneToOne
    @Column(name = "attachment_id", nullable = false)
    private BinaryContentEntity attachmentId;

}
