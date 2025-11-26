package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusControllerDocs {

  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(
              schema = @Schema(implementation = ReadStatusResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "이미 읽음 상태가 존재함",
          content = @Content(
              examples = @ExampleObject(
                  value = "ReadStatus with userId {userId} and channelId {channelId} already exists"
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "Channel | User with id {channelId | userId} not found"
              )
          )
      )
  })
  ResponseEntity<ReadStatusResponseDto> ChannelByCreateReadStatus(
      @RequestBody CreateReadStatusRequestDto request);


  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(
              schema = @Schema(implementation = ReadStatusResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "ReadStatus with id {readStatusId} not found"
              )
          )
      )
  })
  ResponseEntity<ReadStatusResponseDto> ChannelByUpdateReadStatus(
      @Parameter(description = "수정할 읽음 상태 ID")
      @PathVariable UUID readStatusId,
      @RequestBody UpdateReadStatusDto request);


  @Operation(summary = "User의 Massage 읽음 상태 목록 조회")
  @ApiResponse(
      responseCode = "200",
      description = "Massage 읽음 상태 목록 조회 성공",
      content = @Content(
          schema = @Schema(implementation = ReadStatusResponseDto.class, type = "array")
      )
  )
  ResponseEntity<List<ReadStatusResponseDto>> UserByFindMessage(
      @Parameter(description = "조회할 User ID")
      @RequestParam UUID userId);

}
