package com.sprint.mission.discodeit.message.channel;

import com.sprint.mission.discodeit.common.entity.BaseEntity;

import java.util.UUID;

public class ChannelMessage extends BaseEntity<UUID> {

    private UUID channelId;
    private UUID senderId;
    private String message;

    /**
     * 외부에서의 직접적인 생성을 막고, static create 메서드를 통하도록 강제합니다.
     */
    private ChannelMessage() {
        super(UUID.randomUUID());
    }

    /**
     * ChannelMessage 객체를 안전하게 생성하는 정적 팩토리 메서드입니다.
     *
     * @param channelId 메시지가 속한 채널의 ID (필수)
     * @param senderId  메시지를 보내는 사용자의 ID (필수)
     * @param message   메시지 내용 (필수)
     * @return 완전히 생성된 ChannelMessage 객체
     */
    public static ChannelMessage create(UUID channelId, UUID senderId, String message) {
        if (channelId == null) {
            throw new IllegalArgumentException("채널 ID는 필수입니다.");
        }
        if (senderId == null) {
            throw new IllegalArgumentException("보내는 사람의 ID는 필수입니다.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("메시지 내용은 비어있을 수 없습니다.");
        }

        ChannelMessage channelMessage = new ChannelMessage();
        channelMessage.channelId = channelId;
        channelMessage.senderId = senderId;
        channelMessage.message = message;
        return channelMessage;
    }

    // --- Getters ---

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChannelMessage{" +
                "id=" + getId() +
                ", channelId=" + channelId +
                ", senderId=" + senderId +
                ", message='" + message + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}