package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "binary_contents")
@AttributeOverride(name = "id", column = @Column(name = "binary_content_id"))
public class BinaryContent extends BaseEntity {

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;


    @Column(name = "file_type", length = 50,nullable = false)
    private String fileType;

    @Column(name = "file_size",nullable = false)
    private long fileSize;
}
