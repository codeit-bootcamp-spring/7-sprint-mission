package com.sprint.mission.discodeit.dto.binaryContent.request;

import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
        @NotNull(message = "업로드 유저는 필수입니다.")
        String userId,
        @NotNull(message = "파일 url은 필수입니다.")
        String fileUrl // db가 없으므로 url을 받아 저장하는 형식으로 사용
) { }
