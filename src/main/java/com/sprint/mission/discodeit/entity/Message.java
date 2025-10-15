package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import java.io.Serializable;

public class Message implements Serializable {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private UUID channelId; // 메시지가 속한 Channel의 ID
    private UUID userId;    // 메시지를 보낸 User의 ID
    private String content;

    public Message(UUID channelId, UUID userId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.channelId = channelId;
        this.userId = userId;
        this.content = content;
    }

    // Getter 함수들
    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public UUID getChannelId() { return channelId; }
    public UUID getUserId() { return userId; }
    public String getContent() { return content; }

    // 필드 수정 함수
    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Message{id=" + id.toString().substring(0, 8) +
                ", content='" + content.substring(0, Math.min(content.length(), 20)) + "...'" +
                ", userId=" + userId.toString().substring(0, 8) +
                ", channelId=" + channelId.toString().substring(0, 8) +
                ", updatedAt=" + updatedAt + '}';
    }
}