package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter @ToString
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinaryContent extends BaseEntity {
    /* 10MB, 500MB 업로드 제한
    private static final long BASIC_MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final long NITRO_MAX_FILE_SIZE = 500 * 1024 * 1024;

    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;
    private static final long MAX_FILE_SIZE_MB = 10;
    private static final long BASIC_MAX_FILE_SIZE = MAX_FILE_SIZE_MB * BYTES_PER_MB;
    */

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(length = 100, nullable = false)
    private String contentType;
}
