package com.sprint.mission.discodeit.dto.user;

/**
 * 프로필 이미지 업로드용 DTO (record)
 * - filename: 원본 파일명 (예: avatar.png)
 * - contentType: MIME 타입 (예: image/png)
 * - data: 파일 바이트 데이터
 */
public record ProfileImageCreateRequest(
        String filename,
        String contentType,
        byte[] data
) {}