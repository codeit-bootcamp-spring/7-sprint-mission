package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "message_attachments")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
public class MessageAttachments {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    Message message;

    @OneToOne
    @JoinColumn(name = "attachment_id", nullable = false)
    BinaryContent binaryContent;



//    public MessageAttachments(UUID id, Message message, BinaryContent binaryContent) {
//        this.id = id;
//        this.message = message;
//        this.binaryContent = binaryContent;
//    }

    public void changeMessage(Message message) {
        this.message = message;
        message.getMessageAttachmentList().add(this);
    }

    public void changeBinaryContent(BinaryContent binaryContent) {
        this.binaryContent = binaryContent;
        binaryContent.setAttachments(this);
    }
}
