package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent")
public interface BinaryContentControllerDocs {

    @Operation(summary = "여러가지 파일 조회")
    List<BinaryContentResponse> getFile(List<UUID> binaryContentId);

    @Operation(summary = "한 가지 파일 조회")
    BinaryContentResponse getFile(UUID binaryContentId);
}
