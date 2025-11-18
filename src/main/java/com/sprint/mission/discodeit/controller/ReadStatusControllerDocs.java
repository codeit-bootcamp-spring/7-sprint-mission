package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "읽음 상태 관리(Read Status Controller)",description = "읽음 상태를 관리하는 API 입니다.")
public interface ReadStatusControllerDocs {
    @Operation(summary = "읽음 상태 생성(Create Read Status)", description = "읽음 상태를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 상태 생성 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadStatusCreateRequestDto.class),
    examples = @ExampleObject(value = """
            {"channelId":"a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412",
            "userId":"a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412",
            "lastReadAt":"2025-11-13T10:30:00Z"}
            """)
    )
    )
    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity<ReadStatusResponseDto> createReadStatus(@RequestBody ReadStatusCreateRequestDto dto);

    @Operation(summary = "읽음 상태 조회(Read Read Status)", description = "읽음 상태를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공",
    content = @Content(mediaType = "application/json",
    examples = @ExampleObject(value = """
            쿼리스트링으로 userId를 잘 전달합시다.
            """)
    )
    )
    @RequestMapping(value = "", method = RequestMethod.GET)
    ResponseEntity<List<ReadStatusResponseDto>> readReadStatus(@RequestParam UUID userId);

    @Operation(summary = "읽음 상태 수정(Patch Read Status)", description = "읽음 상태를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 상태 수정 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadStatusPatchRequestDto.class),
    examples = @ExampleObject(value = """
            
            {"newLastReadAt":"2025-11-13T10:30:00Z"}
            """)
    )
    )
    @RequestMapping(value = "/{readStatusId}",method = RequestMethod.PATCH)
    ResponseEntity<ReadStatusResponseDto> patchReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusPatchRequestDto dto);
}
