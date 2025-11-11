package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_Message;
import com.sprint.mission.discodeit.entity.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_Message;
import com.sprint.mission.discodeit.service.basic.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    //    메시지 관리
//    [ ] 메시지를 보낼 수 있다.
//    [ ] 메시지를 수정할 수 있다.
//    [ ] 메시지를 삭제할 수 있다.
//    [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
    @RequestMapping(value = "/create", method = POST)
    public Res_Message create(@RequestPart("message") Dto_Message dtoMessage,
                              @RequestPart("file") List<MultipartFile> fileList) {

        List<Dto_BinaryContent> collect = fileList.stream()
                .map(Util::parsingMultipartFile)
                .toList(); // 변경가능 list

        return messageService.create(dtoMessage, Optional.of(collect));
    }

    @RequestMapping(value = "/findAllByChannelId/{id}", method = GET)
    public List<Res_Message> findAllByChannleId(@PathVariable("id") UUID channelID) {
        return messageService.findAllByChannleId(channelID);
    }

    @RequestMapping(value = "/find/{id}", method = GET)
    public Res_Message find(@PathVariable("id") UUID messageID) {
        return messageService.find(messageID);
    }

    @RequestMapping(value = "/update", method = PUT)
    public Res_Message updateMessage(@RequestBody Dto_MessageUpdate requestDto) {
        return messageService.updateMessage(requestDto);
    }

    @RequestMapping(value = "/delete/{id}", method = DELETE)
    public void deleteMessage(@PathVariable("id") UUID messageID) {
        messageService.deleteMessage(messageID);
    }
}