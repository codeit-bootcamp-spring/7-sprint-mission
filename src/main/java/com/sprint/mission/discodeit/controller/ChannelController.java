package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePrivateResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePublicResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channel")
@AllArgsConstructor
public class ChannelController {

    private final ChannelService channelService;


    // [공개채널 생성]
   @PostMapping("/createpublic")
    public ChannelCreatePublicResponse createPublicChannel(@RequestBody ChannelCreateRequest request) {
          return channelService.createPublicChannel(request);
   }
    // [비공개 채널 생성]
    @PostMapping("/createprivate")
    public ChannelCreatePrivateResponse createPrivateChannel(@RequestBody ChannelCreateRequest request) {
        return channelService.createPrivateChannel(request);
    }

    // [공개채널 정보 확인]

    // [채널 삭제]

    // [특정 사용자가 볼 수 있는 모든 채널 목록 조회]


}

