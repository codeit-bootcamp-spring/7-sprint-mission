package com.sprint.mission.discodeit.controller.doc;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageDocs {

  @Operation(summary = "Message 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨", content = @Content(mediaType = "*/*", schema = @Schema(implementation = MessageResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음", content = @Content(mediaType = "*/*", schema = @Schema(implementation = CustomApiResponse.class)))})
  ResponseEntity<MessageResponseDto> createMessage(
      @Parameter(description = "Message 생성 정보", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @Valid @RequestPart("messageCreateRequest") CreateMessageDto createMessageDto,
      @Parameter(description = "Message 첨부 파일들", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "attachments") List<MultipartFile> attachments);

  @Operation(summary = "Message 내용 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = MessageResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = CustomApiResponse.class)))})
  ResponseEntity<MessageResponseDto> updateMessage(
      @Parameter(description = "수정할 Message ID") @PathVariable UUID messageId,
      @Parameter(description = "수정할 Message 내용") @RequestBody UpdateMessageDto updateMessageDto);

  @Operation(summary = "Message 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = CustomApiResponse.class)))})
  ResponseEntity<Void> deleteMessage(
      @Parameter(description = "삭제할 Message ID") @PathVariable UUID messageId);

  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Message 목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = MessageResponseDto.class))))
  })
  ResponseEntity<List<MessageResponseDto>> getMessages(
      @Parameter(description = "조회할 Channel ID") @RequestParam UUID channelId);
}
