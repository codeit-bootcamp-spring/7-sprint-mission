package com.sprint.mission.discodeit.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.sprint.mission.discodeit.controller.swaggerDocs.ChannelDoc;
import com.sprint.mission.discodeit.entity.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPublic;
import com.sprint.mission.discodeit.entity.dto.Res_Channel;
import com.sprint.mission.discodeit.entity.dto.Res_ChannelFind;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelDoc {
    private final ChannelService channelService;

    //💎Public Channel 생성
    @RequestMapping(value = "/public", method = POST)
    public ResponseEntity<Res_Channel> createPublic(
        @RequestBody Dto_CreateChannelPublic dtoCreateChannel) {
        Res_Channel aPublic = channelService.createPublic(dtoCreateChannel);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(aPublic);
    }

    //💎Private Channel 생성
    @RequestMapping(value = "/private", method = POST)
    public ResponseEntity<Res_Channel> createPrivate(@
        RequestBody Dto_CreateChannelPrivate dtoCreateChannel) {
        Res_Channel channel = channelService.createPrivate(dtoCreateChannel);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channel);
    }

    //💎Channel 삭제
    @RequestMapping(value = "/{channelId}", method = DELETE)
    public ResponseEntity<Object> delete(
        @PathVariable("channelId") UUID channelId) {
      channelService.delete(channelId);
      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    }

    //💎Channel 정보 수정
    @RequestMapping(value = "/{channelId}", method = PATCH)
    public ResponseEntity<Res_Channel> update(
        @PathVariable("channelId") UUID channelId,
        @RequestBody ChannelDto_Update channelDtoUpdate) {

        Res_Channel resChannel = channelService.update(channelId, channelDtoUpdate);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resChannel);
    }

    //💎User가 참여 중인 Channel 목록 조회
    @GetMapping
    public ResponseEntity<List<Res_ChannelFind>> findAllByUserId(
        @RequestParam("userId") UUID userId) {

        List<Res_ChannelFind> allByUserId = channelService.findAllByUserId(userId);
        System.out.println(allByUserId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(allByUserId);
    }
}