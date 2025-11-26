package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import com.sprint.mission.discodeit.swaggerDocs.MessageDoc;
import com.sprint.mission.discodeit.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.basic.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController //👍 @controller + @responsebody
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Slf4j
public class MessageController implements MessageDoc {
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDto>> findAllByChannelId(
        @RequestParam("channelId") UUID channelID) {

        //💎♨️Channel의 Message 목록 조회

        List<MessageDto> allByChannleId = messageService.findAllByChannelId(channelID);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(allByChannleId);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<MessageDto> create(
        @Valid @RequestPart("messageCreateRequest") MessageCreateRequest dtoMessage,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> fileList) {

        //💎Message 생성
        List<Dto_BinaryContent> collect = Util.parsingMultipartFileList(fileList);

        MessageDto resMessage = messageService.create(dtoMessage, Optional.ofNullable(collect));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(resMessage);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Object> deleteMessage(
        @PathVariable("messageId") UUID messageId) {
        //💎Message 삭제
        messageService.deleteMessage(messageId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(
        @PathVariable("messageId") UUID messageId,
        @Valid @RequestBody Dto_MessageUpdate requestDto) {

        //💎Message 내용 수정
        MessageDto resMessage = messageService.updateMessage(messageId, requestDto);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resMessage);
    }
}