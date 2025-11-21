package com.sprint.mission.discodeit.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


//지금처럼 하는 것 vs @OneToMany사용
@Table(name = "message_attachments")
@Entity
public class MessageAttachmentEntity extends BaseEntity{

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "message_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MessageEntity messageId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "attachment_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BinaryContentEntity attachmentId;

}
