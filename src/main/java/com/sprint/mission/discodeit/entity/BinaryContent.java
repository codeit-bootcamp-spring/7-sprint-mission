package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 바이너리 데이터(프로필 이미지, 메시지 첨부 등)
 * - 수정 불가(Immutable) 모델 → updatedAt 미정의
 * - 공통 필드 중 id, createdAt만 유지
 * - 의존 방향: User/Message가 BinaryContent의 ID를 참조
 *   (BinaryContent는 역참조를 갖지 않음)
 */
@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updatedAt;

    private final String filename;
    private final String contentType;
    private final long sizeBytes;
    private final byte[] data;

    public BinaryContent(String filename, String contentType, long sizeBytes, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.filename = filename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.data = data;
    }
}