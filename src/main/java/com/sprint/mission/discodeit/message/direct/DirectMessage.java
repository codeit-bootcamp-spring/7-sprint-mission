package com.sprint.mission.discodeit.message.direct;

import com.sprint.mission.discodeit.common.entity.BaseEntity;

import java.util.UUID;

public class DirectMessage extends BaseEntity<UUID> {

    private UUID receiverId;
    private UUID senderId;
    private String message;

    /**
     * 외부에서의 직접적인 생성을 막고, static create 메서드를 통하도록 강제합니다.
     */
    private DirectMessage() {
        super(UUID.randomUUID());
    }

    /**
     * DirectMessage 객체를 안전하게 생성하는 정적 팩토리 메서드입니다.
     *
     * @param senderId   보내는 사용자 ID (필수)
     * @param receiverId 받는 사용자 ID (필수)
     * @param message    메시지 내용 (필수)
     * @return 완전히 생성된 DirectMessage 객체
     */
    public static DirectMessage create(UUID senderId, UUID receiverId, String message) {
        if (senderId == null) {
            throw new IllegalArgumentException("보내는 사람의 ID는 필수입니다.");
        }
        if (receiverId == null) {
            throw new IllegalArgumentException("받는 사람의 ID는 필수입니다.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("메시지 내용은 비어있을 수 없습니다.");
        }

        DirectMessage directMessage = new DirectMessage();
        directMessage.senderId = senderId;
        directMessage.receiverId = receiverId;
        directMessage.message = message;
        return directMessage;
    }

    // --- Getters ---

    public UUID getReceiverId() {
        return receiverId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DirectMessage{" +
                "id=" + getId() +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", message='" + message + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}