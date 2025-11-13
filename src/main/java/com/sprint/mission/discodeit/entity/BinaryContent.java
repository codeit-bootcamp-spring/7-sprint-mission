package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.FileSizeLimitExceededException;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    /* 10MB, 500MB 업로드 제한
    private static final long BASIC_MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final long NITRO_MAX_FILE_SIZE = 500 * 1024 * 1024;
     */
    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;
    private static final long MAX_FILE_SIZE_MB = 10;
    private static final long BASIC_MAX_FILE_SIZE = MAX_FILE_SIZE_MB * BYTES_PER_MB;

    private final UUID id;
    private final Instant createAt;

    private final transient byte[] binaryData;
    private final String dataName;
    private final String dataType;    // txt, zip, jpg등

    @Builder
    public BinaryContent(byte[] binaryData, String dataName, String dataType) {

        validateData(binaryData);

        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.binaryData = binaryData;
        this.dataName = dataName;
        this.dataType = dataType;
    }

    private void validateData(byte[] binaryData) {
        if (binaryData == null || binaryData.length == 0) {
            throw new InvalidInputException("data is empty"); // 임시
        }
        // Spring은 자체적으로 1MB 제한을 건대요 그래서 500MB로 늘림, 현재는 기본으로 제한
        if (binaryData.length > BASIC_MAX_FILE_SIZE) {
            throw new FileSizeLimitExceededException("파일용량은 10MB를 넘을 수 없습니다.");
        }
    }
}
