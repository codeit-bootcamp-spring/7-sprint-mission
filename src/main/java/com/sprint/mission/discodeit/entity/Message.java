package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor

@Getter
@Table(name = "messages")
public class Message extends BaseUpdateEntity {

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "messages_author_id_fk"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "messages_channel_id_fk"), nullable = false)
    private Channel channel;

    @Column(columnDefinition = "TEXT")
    private String content;


    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MessageAttachment> attachments = new ArrayList<>();


    public Message(User author, Channel channel, String content) {
        this.author = author;
        this.content = content;
        this.channel = channel;

    }

    public void update(String newContent) {

        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;

        }


    }

    //첨부파일추가
   /* public void addAttachmentId(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
    }*/
    public void addAttachment(BinaryContent content) {
        MessageAttachment ma = new MessageAttachment(this, content);
        attachments.add(ma);
    }

    public void removeAttachment(BinaryContent content) {
        attachments.removeIf(ma -> ma.getAttachment().equals(content));
    }
}
