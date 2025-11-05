package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.*;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
// http://localhost:8080/user-list.html
public class ChannelController  extends BaseController {
    private final ChannelService channelService;
//    채널 관리
//    [ ] 공개 채널을 생성할 수 있다.
//    [ ] 비공개 채널을 생성할 수 있다.
//    [ ] 공개 채널의 정보를 수정할 수 있다.
//    [ ] 채널을 삭제할 수 있다.
//    [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
    @RequestMapping(value = "/createPublic", method = POST)
    public Res_Channel createPublic(@RequestBody Dto_CreateChannelPublic dtoCreateChannel) {
        return channelService.createPublic(dtoCreateChannel);
    }

    @RequestMapping(value = "/createPrivate", method = POST)
    public Res_Channel createPrivate(@RequestBody Dto_CreateChannelPrivate dtoCreateChannel) {
        return channelService.createPrivate(dtoCreateChannel);
    }

    @RequestMapping(value = "/update", method = POST)
    public void update(@RequestBody Dto_ChannelUpdate dtoChannelUpdate) {
        channelService.update(dtoChannelUpdate);
    }

    @RequestMapping(value = "/delete/{id}", method = DELETE)
    public void delete(@PathVariable("id") UUID uuid) {
        channelService.delete(uuid);
    }

    @RequestMapping(value = "/findAllByUserId/{id}", method = POST)
    public List<Res_ChannelFind> findAllByUserId(@PathVariable("id") UUID userID) {
        return channelService.findAllByUserId(userID);
    }
}
