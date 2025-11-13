package com.sprint.mission.discodeit.dto.request.binaryContent;

import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BinaryContentCreateRequestDto {
    @NotBlank
    private final byte[] bytes;
    @NotBlank
    private String fileName;
    @NotBlank
    private Long size;
    @NotBlank
    private String contentType;
}
