package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.mapper.dto.ChannelDto;
import com.sprint.mission.discodeit.service.InterfaceChannelService;
import com.sprint.mission.discodeit.swaggerDocs.ChannelDoc;
import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.mapper.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.mapper.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController //👍 @controller + @responsebody
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelDoc {
    private final InterfaceChannelService channelService;

    //💎Public Channel 생성
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(
        @Valid @RequestBody PublicChannelCreateRequest dtoCreateChannel) {

        ChannelDto aPublic = channelService.createPublic(dtoCreateChannel);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(aPublic);
    }

    //💎Private Channel 생성
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivate(
        @Valid @RequestBody PrivateChannelCreateRequest dtoCreateChannel) {

        ChannelDto channel  = channelService.createPrivate(dtoCreateChannel);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channel);
    }

    //💎Channel 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Object> delete(
        @PathVariable("channelId") UUID channelId) {

      channelService.delete(channelId);

      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    }

    //💎Channel 정보 수정
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> update(
        @PathVariable("channelId") UUID channelId,
        @Valid @RequestBody ChannelDto_Update channelDtoUpdate) {

        ChannelDto resChannel  = channelService.update(channelId, channelDtoUpdate);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resChannel);
    }

    //💎User가 참여 중인 Channel 목록 조회
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllByUserId(
        @RequestParam("userId") UUID userId) {

        List<ChannelDto> allByUserId  = channelService.findAllByUserId(userId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(allByUserId);
    }
}