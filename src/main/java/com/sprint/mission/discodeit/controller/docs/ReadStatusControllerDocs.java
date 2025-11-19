package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus")
public interface ReadStatusControllerDocs {

    @Operation(summary = "User의 Message 읽음 상태 조회")
    List<ReadStatusResponse> getReadStatus(UUID userId);

    @Operation(summary = "Message 읽음 상태 수정")
    ReadStatusResponse readChannel(UUID id);

    @Operation(summary = "읽음 상태 생성")
    ReadStatusResponse createReadStatus(ReadStatusCreateRequest request);
}
