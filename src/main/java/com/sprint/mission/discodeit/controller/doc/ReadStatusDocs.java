package com.sprint.mission.discodeit.controller.doc;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.global.dto.CustomApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusDocs {

  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = ReadStatusResponseDto.class))),
      @ApiResponse(responseCode = "400",
          description = "이미 읽음 상태가 존재함",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = CustomApiResponse.class))
      ),
      @ApiResponse(responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = CustomApiResponse.class))
      )
  })
  ResponseEntity<ReadStatusResponseDto> createReadStatus(
      @Parameter(description = "Message 읽음 상태 생성 정보")
      @Valid @RequestBody CreateReadStatusDto createReadStatusDto);

  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = ReadStatusResponseDto.class))),
      @ApiResponse(responseCode = "404",
          description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = CustomApiResponse.class))
      )
  })
  ResponseEntity<ReadStatusResponseDto> updateReadStatus(
      @Parameter(description = "수정할 읽음 상태 ID") @PathVariable UUID readStatusId,
      @Parameter(description = "수정할 읽음 상태 정보") @RequestBody UpdateReadStatusDto updateReadStatusDto);

  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Message 읽음 상태 목록 조회 성공",
          content = @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = ReadStatusResponseDto.class)))),
  })
  ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatus(
      @Parameter(description = "조회할 User ID") @RequestParam UUID userId);
}
