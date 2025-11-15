package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageInfoReq;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.facade.message.MessageCreationFacade;
import com.sprint.mission.discodeit.facade.message.MessageDeleteFacade;
import com.sprint.mission.discodeit.facade.message.MessageOverviewFacade;
import com.sprint.mission.discodeit.facade.message.MessageUpdateFacade;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageCreationFacade messageCreationFacade;
  private final MessageOverviewFacade messageOverviewFacade;
  private final MessageUpdateFacade messageUpdateFacade;
  private final MessageDeleteFacade messageDeleteFacade;

  //특정 채널의 메세지들 조회
  @GetMapping
  public ResponseEntity<List<MessageViewRes>> findAllByChannelId(@RequestParam UUID channelId) {
    return ResponseEntity.ok(messageOverviewFacade.findAllByChannelId(channelId));
  }

  //메세지 입력
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageViewRes> createMessage(
      @RequestHeader("X-LOGINUSER-ID") UUID speakerId,
      @RequestParam UUID channelId,
      @Valid @RequestPart("messageInfoReq") MessageInfoReq messageInfoReq,
      @RequestPart(value = "attachmentFiles", required = false) List<MultipartFile> attachmentFiles) {

    List<BinaryContentCreateReq> binaryContentCreateReqs = attachmentFiles == null ?
        List.of() : attachmentFiles.stream().map(BinaryContentCreateReq::from).toList();
    MessageCreateReq req = MessageCreateReq.from(messageInfoReq, binaryContentCreateReqs);
    MessageViewRes message = messageCreationFacade.createMessage(speakerId, channelId, req);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand(message.messageId())
        .toUri();
    return ResponseEntity.created(location).body(message);
  }

  //메세지 수정
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageViewRes> updateMessage(
      @PathVariable UUID messageId,
      @Valid @RequestPart("messageInfoReq") MessageInfoReq messageInfoReq,
      @RequestPart(value = "attachmentIds", required = false) List<UUID> keepAttachmentIds,
      @RequestPart(value = "attachmentFiles", required = false) List<MultipartFile> newAttachmentReqs) {

    List<BinaryContentCreateReq> binaryContentCreateReqs = newAttachmentReqs == null ?
        List.of() : newAttachmentReqs.stream().map(BinaryContentCreateReq::from).toList();
    MessageUpdateReq req = new MessageUpdateReq(
        messageInfoReq.content(), keepAttachmentIds, binaryContentCreateReqs);
    MessageViewRes res = messageUpdateFacade.updateMessage(messageId, req);
    return ResponseEntity.ok(res);
  }

  //메세지 삭제
  @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageDeleteFacade.deleteMessage(messageId);
    return ResponseEntity.noContent().build();
  }
}
