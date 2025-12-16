package com.sprint.mission.discodeit.dto.request.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BinaryContentCreateRequestDto {

    @NotNull(message = "파일 바이트는 필수값입니다")
    private final byte[] bytes;

    @NotBlank(message = "파일 이름은 필수값입니다")
    private String fileName;

    @Positive(message = "파일 크기는 0보다 커야합니다.")
    @NotNull(message = "파일 크기는 필수값입니다")
    private Long size;

    @NotBlank(message = "파일 컨텐츠 타입은 필수값입니다")
    private String contentType;
}
