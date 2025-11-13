package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.MessageDocs;
import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageDocs {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponseDto> createMessage(
      @Valid @RequestPart("messageCreateRequest") CreateMessageDto createMessageDto,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    List<CreateBinaryContentDto> createBinaryContentDtos =
        Stream.ofNullable(attachments)
            .flatMap(Collection::stream)
            .map(attachment -> {
              try {
                return new CreateBinaryContentDto(
                    attachment.getOriginalFilename(),
                    attachment.getContentType(),
                    attachment.getSize(),
                    attachment.getBytes());
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList();

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
