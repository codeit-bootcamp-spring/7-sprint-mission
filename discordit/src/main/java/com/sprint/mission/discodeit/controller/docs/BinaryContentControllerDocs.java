package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "바이너리 컨텐츠 관리", description = "파일 및 바이너리 컨텐츠 조회 API입니다.")
public interface BinaryContentControllerDocs {
    @Operation(
            summary = "바이너리 컨텐츠 조회",
            description = "특정 ID의 바이너리 컨텐츠를 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<BinaryContentResponse> getContent(@PathVariable UUID binaryContentId);

    @Operation(
            summary = "여러 바이너리 컨텐츠 조회",
            description = "여러 ID의 바이너리 컨텐츠를 한번에 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<BinaryContentResponse>> getContents(@RequestParam List<UUID> binaryContentIds);

    @Operation(
            summary = "모든 바이너리 컨텐츠 조회",
            description = "시스템의 모든 바이너리 컨텐츠를 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<BinaryContentResponse>> getAll();
}