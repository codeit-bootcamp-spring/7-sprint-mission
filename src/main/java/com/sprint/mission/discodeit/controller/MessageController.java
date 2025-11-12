package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.MessageDocs;
import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageDocs {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponseDto> createMessage(
      @Valid @RequestPart("messageCreateRequest") CreateMessageDto createMessageDto,
      @RequestPart(value = "attachments") List<MultipartFile> attachments) {
    List<CreateBinaryContentDto> createBinaryContentDtos = attachments.stream()
        .map(attachment -> {
          CreateBinaryContentDto createBinaryContentDto = null;
          try {
            createBinaryContentDto = new CreateBinaryContentDto(
                attachment.getOriginalFilename(),
                attachment.getContentType(),
                attachment.getSize(),
                attachment.getBytes());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          return createBinaryContentDto;
        }).toList();

    MessageResponseDto messageResponseDto = messageService.createMessage(createMessageDto,
        createBinaryContentDtos);
    return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDto);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable UUID messageId,
      @RequestBody UpdateMessageDto updateMessageDto) {
    MessageResponseDto messageResponseDto = messageService.updateMessage(messageId,
        updateMessageDto);
    return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<MessageResponseDto>> getMessages(@RequestParam UUID channelId) {
    List<MessageResponseDto> allMessageByChannelId = messageService.getAllMessageByChannelId(
        channelId);
    return ResponseEntity.status(HttpStatus.OK).body(allMessageByChannelId);
  }


}
