package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.Docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelControllerDocs {

    private final ChannelService channelService;


    // [공개채널 생성]

    @RequestMapping(path = "public", method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> create(@RequestBody PublicChannelCreateRequest request) {
        ChannelDto createdChannel = channelService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }
    // [비공개 채널 생성]

    @RequestMapping(path = "private", method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
        ChannelDto createdChannel = channelService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }


    // [공개채널 정보 수정]
    @RequestMapping(path = "{channelId}", method = RequestMethod.PATCH)
    public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
                                             @RequestBody ChannelUpdateRequest request) {
        ChannelDto udpatedChannel = channelService.update(channelId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(udpatedChannel);
    }

    // [채널 삭제]
    @RequestMapping(path = "{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    // [특정 사용자가 볼 수 있는 모든 채널 목록 조회]
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<ChannelDto> allByUserId = channelService.findAllByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allByUserId);
    }

}

