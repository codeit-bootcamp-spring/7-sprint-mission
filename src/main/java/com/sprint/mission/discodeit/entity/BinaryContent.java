package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "binary_contents")
@AttributeOverride(name = "id", column = @Column(name = "binary_content_id"))
public class BinaryContent extends BaseUpdatableEntity {

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;


    @Column(name = "file_type", length = 50,nullable = false)
    private String fileType;

    @Column(name = "file_size",nullable = false)
    private long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BinaryContentStatus status;

    public BinaryContent(String fileName, String fileType, long fileSize) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.status = BinaryContentStatus.PROCESSING;
    }

    public void markSuccess(){
        this.status = BinaryContentStatus.SUCCESS;
    }

    public void markFail(){
        this.status = BinaryContentStatus.FAIL;
    }
}
