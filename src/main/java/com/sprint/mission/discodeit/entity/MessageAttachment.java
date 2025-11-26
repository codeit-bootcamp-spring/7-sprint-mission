package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "message_attachments")
@Entity
@NoArgsConstructor
public class MessageAttachment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @OneToOne
    @Column(name = "attachment_id", nullable = false)
    private BinaryContent attachment;

    public MessageAttachment(Message message, BinaryContent attachment) {
        this.message = message;
        this.attachment = attachment;
    }
}
