package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "binary_contents")
public final class BinaryContent extends BaseUpdatableEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryContentStatus status;

    @Builder
    public BinaryContent(String fileName, Long size, String contentType) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.status = BinaryContentStatus.PROCESSING;
    }

    public void updateStatus(BinaryContentStatus status) {
        this.status = status;
    }
}
