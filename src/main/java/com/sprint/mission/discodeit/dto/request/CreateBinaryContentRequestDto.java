package com.sprint.mission.discodeit.dto.request;

public record CreateBinaryContentRequestDto (

    byte[] data, // 데이터 (byte)
    String fileName, //파일 이름
    String fileType //파일 타입
) {}


