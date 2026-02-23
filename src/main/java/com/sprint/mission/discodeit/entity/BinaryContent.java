package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.status.BinaryContentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseUpdatableEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

//    @Column(nullable = false)
//    private byte[] bytes;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryContentStatus status;

    @Builder
    public BinaryContent(String fileName, String contentType, Long size, BinaryContentStatus status) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.status = status;
    }

    public void updateStatus(BinaryContentStatus status) {
        this.status = status;
    }
}
