package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelDeleteRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelFindByUserIdRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePrivateResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePublicResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelUpdateResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
   @PostMapping("/update")
   public ChannelUpdateResponse channelUpdateResponse(@RequestBody ChannelUpdateRequest request){
      return channelService.update(request);
   }
    // [채널 삭제]
   @DeleteMapping("/delete")
  public void delete(@RequestBody ChannelDeleteRequest request){ channelService.delete(request.channelId()); }


  // [특정 사용자가 볼 수 있는 모든 채널 목록 조회]
  @PostMapping("/findallbyuserid")
  public List<ChannelFindResponse> findAllByUserId(@RequestBody ChannelFindByUserIdRequest request){
       return channelService.findAllByUserId(request.userId());
   }

}

