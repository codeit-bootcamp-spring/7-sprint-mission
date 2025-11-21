package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.FileSizeLimitExceededException;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
public class BinaryContent extends BaseEntity {
    /* 10MB, 500MB 업로드 제한
    private static final long BASIC_MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final long NITRO_MAX_FILE_SIZE = 500 * 1024 * 1024;
     */
    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;
    private static final long MAX_FILE_SIZE_MB = 10;
    private static final long BASIC_MAX_FILE_SIZE = MAX_FILE_SIZE_MB * BYTES_PER_MB;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(length = 100, nullable = false)
    private String contentType;

    @Lob
    @Column(nullable = false)
    private byte[] bytes;

    @Builder
    public BinaryContent(byte[] bytes, String fileName, String contentType) {
        super();

        validateData(bytes);
        this.fileName = fileName;
        this.size = (long) bytes.length;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    private void validateData(byte[] binaryData) {
        if (binaryData == null || binaryData.length == 0) {
            throw new InvalidInputException("파일이 비었습니다.");
        }
        // Spring은 자체적으로 1MB 제한을 건대요 그래서 500MB로 늘림, 현재는 기본으로 제한
        if (binaryData.length > BASIC_MAX_FILE_SIZE) {
            throw new FileSizeLimitExceededException("파일용량은 10MB를 넘을 수 없습니다.");
        }
    }
}
