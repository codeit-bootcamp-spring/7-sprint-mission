package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController implements MessageControllerDocs {

  private final MessageService messageService;


  // 메시지를 보낼 수 있다.
  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<MessageResponseDto> createMessage(
      @RequestPart("messageCreateRequest") CreateMessageRequestDto messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {
    log.info("POST /api/messages - 메시지 생성 요청");
    MessageResponseDto message = messageService.createMessage(messageCreateRequest, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(message);
  }

  //메시지를 수정할 수 있다.
  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageResponseDto> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody UpdateMessageDto MessageUpdateRequest) {
    MessageResponseDto response = messageService.updateMessage(messageId,
        MessageUpdateRequest);
    log.info("PATCH api/messages/{} - 메시지 수정 요청", messageId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  //메시지를 삭제할 수 있다.
  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable UUID messageId) {
    log.info("DELETE api/messages/{}", messageId);
    messageService.deleteMessage(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  //특정 채널의 메시지 목록을 조회할 수 있다.
  // api/messages?channelId=..
  @GetMapping
  public ResponseEntity<PageResponse<MessageResponseDto>> findAllByChannelId(
      @RequestParam UUID channelId,
      @PageableDefault(
          size = 50,
          sort = "createdAt",
          direction = Sort.Direction.DESC) Pageable pageable) {
    PageResponse<MessageResponseDto> response = messageService.findAllByChannelId(channelId,
        pageable);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }


}
