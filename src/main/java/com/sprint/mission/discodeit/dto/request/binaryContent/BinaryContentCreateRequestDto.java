package com.sprint.mission.discodeit.dto.request.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BinaryContentCreateRequestDto {

    @NotNull(message = "Binary bytes")
    private final byte[] bytes;

    @NotBlank(message = "Binary filename")
    private String fileName;

    @NotNull(message = "Binary size")
    private Long size;

    @NotBlank(message = "Binary content type")
    private String contentType;
}
