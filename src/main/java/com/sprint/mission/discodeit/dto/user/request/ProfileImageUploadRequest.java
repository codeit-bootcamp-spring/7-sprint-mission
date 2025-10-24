package com.sprint.mission.discodeit.dto.user.request;

public record ProfileImageUploadRequest(
        String filename,
        String contentType,
        long size,
        String storageKey // 업로드된 파일을 가리키는 키(연습이면 가짜 문자열 OK)
) {

}
