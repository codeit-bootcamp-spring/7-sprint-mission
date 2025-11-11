package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelDeleteRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelFindByUserIdRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePrivateResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePublicResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelUpdateResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;


    // [공개채널 생성]

   @RequestMapping(value = "createPublic", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPublicChannel(@RequestBody ChannelCreateRequest request) {
       Channel publicChannel = channelService.createPublicChannel(request);

       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(publicChannel);
   }
    // [비공개 채널 생성]

    @RequestMapping(value = "/createprivate", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody ChannelCreateRequest request) {
        return channelService.createPrivateChannel(request);
    }

    // [공개채널 정보 수정]
   @RequestMapping(value = "/update", method = RequestMethod.POST)
   public ChannelUpdateResponse channelUpdateResponse(@RequestBody ChannelUpdateRequest request){
      return channelService.update(request);
   }
    // [채널 삭제]
   @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public void delete(@RequestBody ChannelDeleteRequest request){ channelService.delete(request.channelId()); }


  // [특정 사용자가 볼 수 있는 모든 채널 목록 조회]
  @RequestMapping(value = "/findallbyuserid", method = RequestMethod.GET)
  public List<ChannelFindResponse> findAllByUserId(@RequestBody ChannelFindByUserIdRequest request){
       return channelService.findAllByUserId(request.userId());
   }

}

