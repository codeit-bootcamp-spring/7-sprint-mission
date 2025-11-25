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
    private final List<BinaryContent> attachments = new ArrayList<>();


    public Message(User author, Channel channel, String content, List<BinaryContent> attachments) {
        this.author = author;
        this.content = content;
        this.channel = channel;

        if (attachments != null) {
            attachments.forEach(this::addAttachment);
        }
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }


    }

    //첨부파일추가
   /* public void addAttachmentId(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
    }*/
    public void addAttachment(BinaryContent file) {
        this.attachments.add(file);
    }
}
