package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

/**
 * REST 스타일 업데이트 요청 DTO (record)
 * - REST 관례에 맞춰 id는 보통 PathVariable(`/users/{id}`)로 받고,
 *   이 DTO는 "수정할 값들"만 본문으로 전달합니다.
 * - 프로필 이미지는 선택값이며(null 허용), 전달되면 기존 이미지를 대체합니다.
 */
public record UserUpdateRequest(
        UUID userId,
        String name,
        String email,
        String password,
        ProfileImageCreateRequest profileImage // 선택(없으면 null)
) {}
