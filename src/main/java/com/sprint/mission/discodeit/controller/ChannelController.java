package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final BasicChannelService channelService;


    @PostMapping("/public")
    public

    @GetMapping("/messages/{channelId}")
    public List<String> getAllMessage(@PathVariable UUID channelId){
        return channelService.getAllMessage(channelId);
    }

//    @GetMapping
//    public
}
