package com.sprint.mission.discodeit.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserCreateRequestDto(
        String email,
        String password,
        String username,
        String phoneNumber,
        MultipartFile profileImage // 선택적: null이면 이미지 없음


    //      file.getBytes();            // 파일 내용을 byte 배열로 반환
    //      file.getInputStream();      // InputStream으로 읽기
    //      file.isEmpty();             // 파일이 비었는지 확인
    //      file.transferTo(destFile);  // 파일 시스템에 저장

    //  getOriginalFilename() → 원본 파일 이름
    //  getContentType() → MIME 타입 (image/png, text/plain 등)
    //  getSize() → 파일 크기
    //  isEmpty() → 파일이 비었는지 체크

)
 {}