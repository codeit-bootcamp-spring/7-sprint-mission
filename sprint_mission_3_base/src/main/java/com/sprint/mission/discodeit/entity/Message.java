package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 도메인: 메시지
 * - 기본 텍스트 메시지(attachments 선택)만 다룹니다.
 * - 현재 요구사항에 맞춰 최소한의 필드/메서드만 구현했습니다.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    // ---- 식별자/시간 ----
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    // ---- 내용/주인 ----
    private final String content;
    private final UUID authorId;
    private final UUID channelId;

    // ---- 첨부(타입 고정 X: 이후 확장 대비) ----
    private final List<Object> attachments;

    private Message(UUID id,
                    Instant createdAt,
                    Instant updatedAt,
                    String content,
                    UUID authorId,
                    UUID channelId,
                    List<Object> attachments) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachments = attachments == null ? List.of() : Collections.unmodifiableList(attachments);
    }

    /**
     * BasicMessageService에서 호출하는 팩토리.
     * 현재 첨부는 구체 타입이 정해져 있지 않으므로 일단 보관만 합니다.
     * (요구사항 확장 시 Attachment 도메인/DTO로 매핑하세요)
     */
    public static Message createText(String content,
                                     UUID authorId,
                                     UUID channelId,
                                     List<?> attachmentCreateRequests) {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        // 첨부는 안전하게 Object로 보관(서비스/DTO에서 필요 시 변환)
        List<Object> atts = new ArrayList<>();
        if (attachmentCreateRequests != null) {
            atts.addAll(attachmentCreateRequests);
        }

        return new Message(id, now, now, content, authorId, channelId, atts);
    }

    // ====== getters ======
    public UUID getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getContent() { return content; }
    public UUID getAuthorId() { return authorId; }
    public UUID getChannelId() { return channelId; }
    public List<Object> getAttachments() { return attachments; }

    // 내용/첨부 변경 시 갱신(옵션)
    public void update(String newContent, List<?> newAttachments) {
        boolean changed = false;
        if (newContent != null && !newContent.equals(this.content)) {
            // content가 final이라면 별도 빌더/복사 전략 사용하세요.
            throw new UnsupportedOperationException("content는 불변으로 유지했습니다. 필요 시 설계 변경하세요.");
        }
        if (newAttachments != null) {
            // 불변 컬렉션 재생성
            List<Object> copy = new ArrayList<>(newAttachments);
            // attachments가 final이라면 설계 변경 필요. (현 요구사항에선 update 사용 안 함)
            throw new UnsupportedOperationException("attachments는 불변으로 유지했습니다. 필요 시 설계 변경하세요.");
        }
        if (changed) {
            this.updatedAt = Instant.now();
        }
    }
}
