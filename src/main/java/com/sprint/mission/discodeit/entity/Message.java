package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "message_attachments",                    // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "message_id"),  // 내가 연결될 때 사용할 FK
            inverseJoinColumns = @JoinColumn(name = "attachment_id") // 반대쪽 엔티티의 FK
    )
    private List<BinaryContent> attachments;

/*    public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
        this.attachmentIds = attachmentIds;
    }*/


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
}
