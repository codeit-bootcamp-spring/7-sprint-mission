package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor
public class Message extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private UUID channelId;

    @Column(nullable = false)
    private UUID authorId;

    // 첨부파일 ID 리스트(JSON or 1:N 테이블로 분리 가능)
    @ElementCollection
    @CollectionTable(name = "message_attachments", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "attachment_id")
    private List<UUID> attachmentIds = new ArrayList<>();

    public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds;
    }

    public void update(String newContent) {
        this.content = newContent;
    }
}
