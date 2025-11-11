package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

/**
 * 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다.
 * 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
 */
@Getter
@ToString(callSuper = true)
public final class BinaryContent extends Common {
    @Serial
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String contentType;
    private byte[] binaryContent;

    public BinaryContent(String fileName, String contentType, byte[] binaryContent) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.binaryContent = binaryContent;
    }
}
