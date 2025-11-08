package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final BasicChannelService channelService;

    @RequestMapping(value = "/messages/{channelId}", method = RequestMethod.GET)
    public List<String> getAllMessage(@PathVariable UUID channelId){
        return channelService.getAllMessage(channelId);
    }
}
