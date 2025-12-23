package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.entity.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "읽음 상태 관리", description = "메시지 읽음 상태 관련 API입니다.")
public interface ReadStatusControllerDocs {
    @Operation(
            summary = "사용자별 읽음 상태 조회",
            description = "특정 사용자의 모든 읽음 상태를 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<ReadStatusDto>> getByUserId(@RequestParam UUID userId);

    @Operation(
            summary = "읽음 상태 생성",
            description = "새로운 읽음 상태를 생성합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공")
    })
    ResponseEntity<ReadStatusDto> create(@Valid @RequestBody ReadStatusCreateRequest request);

    @Operation(
            summary = "읽음 상태 수정",
            description = "기존 읽음 상태를 수정합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    ResponseEntity<ReadStatusDto> read(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequest request);
}