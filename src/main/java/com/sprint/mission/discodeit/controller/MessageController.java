package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// @Controller + @ResponseBody > @RestController (REST API 컨트롤러용)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {
  // 1. 목록 조회  GET /api/messages?channelId=...
  // 2. 생성      POST /api/messages (multipart/form-data)
  // 3. 수정      PATCH /api/messages/{messageId}
  // 4. 삭제      DELETE /api/messages/{messageId}
  // @RequestMapping() Only?

  private final MessageService messageService;

  /**
   * Message 생성 API
   * POST /api/messages
   * - multipart/form-data로 messageCreateRequest(JSON)와 attachments(파일 목록)를 함께 받는다.
   * - 첨부 파일이 없을 수도 있으므로 attachments는 선택 값이다.
   * 생성된 Message 엔티티 전체를 201(CREATED) 상태 코드와 함께 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new IllegalStateException("첨부 파일을 읽는 중 오류가 발생했습니다.", e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  /**
   * Message 수정 API
   * PATCH /api/messages/{messageId}
   * - path variable로 수정할 Message ID를 받고
   * - 요청 본문으로 MessageUpdateRequest(JSON)를 받는다.
   * 내용만 수정하며, 성공 시 수정된 Message 엔티티를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.PATCH,
      value = "/{messageId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Message> update(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {
    Message updatedMessage = messageService.update(messageId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedMessage);
  }

  /**
   * Message 삭제 API
   * DELETE /api/messages/{messageId}
   * - path variable로 삭제할 Message ID를 받는다.
   * 삭제 성공 시 204(NO_CONTENT)를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.DELETE,
      value = "/{messageId}"
  )
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  /**
   * 특정 Channel의 Message 목록 조회 API
   * GET /api/messages?channelId={channelId}
   * - query parameter로 Channel ID를 받고,
   * - 해당 채널에 속한 Message 리스트를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<Message>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}
