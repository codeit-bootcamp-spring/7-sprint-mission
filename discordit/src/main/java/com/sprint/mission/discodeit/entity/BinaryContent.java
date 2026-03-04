package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.enums.BinaryContentStatus;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sprint.mission.discodeit.common.enums.BinaryContentStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "binary_contents")
@Getter
public class BinaryContent extends BaseUpdatableEntity {

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false)
    Long size;

    @Column(nullable = false)
    String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BinaryContentStatus status = PROCESSING;

    public void uploadSucceed() {
        this.status = SUCCESS;
    }
    public void uploadFailed() {
        this.status = FAIL;
    }

    public boolean isUploaded() {
        return this.status == SUCCESS;
    }

    public BinaryContent(String fileName, Long size, String contentType) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
    }
}
