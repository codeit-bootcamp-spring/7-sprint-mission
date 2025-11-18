package com.sprint.mission.discodeit.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.swaggerDocs.MessageDoc;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.entity.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.dto.Res_Message;
import com.sprint.mission.discodeit.service.basic.MessageService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController //👍 @controller + @responsebody
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Slf4j
public class MessageController implements MessageDoc {
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Res_Message>> findAllByChannleId(
        @RequestParam("channelId") UUID channelID) {
        //💎Channel의 Message 목록 조회
        List<Res_Message> allByChannleId
            = messageService.findAllByChannleId(channelID);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(allByChannleId);
    }


    @PostMapping
    public ResponseEntity<Res_Message> create(
        @RequestPart("messageCreateRequest") MessageCreateRequest dtoMessage,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> fileList) {

        //💎Message 생성
        List<Dto_BinaryContent> collect = Util.parsingMultipartFileList(fileList);

        Res_Message resMessage
            = messageService.create(dtoMessage, Optional.ofNullable(collect));

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
    public ResponseEntity<Res_Message> updateMessage(
        @PathVariable("messageId") UUID messageId,
        @RequestBody Dto_MessageUpdate requestDto) {
        //💎Message 내용 수정
        Res_Message resMessage
            = messageService.updateMessage(messageId, requestDto);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resMessage);
    }
}