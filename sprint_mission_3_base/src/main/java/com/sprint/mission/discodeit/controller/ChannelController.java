package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/channels")
public class ChannelController {


    private final ChannelService channelService;


    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }


    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ChannelDto createPublic(@RequestBody CreatePublicChannelRequest request) {
        return channelService.createPublic(request);
    }


    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ChannelDto createPrivate(@RequestBody CreatePrivateChannelRequest request) {
        return channelService.createPrivate(request);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ChannelDto find(@PathVariable UUID id) {
        return channelService.find(id);
    }


    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ChannelDto> findByUser(@PathVariable UUID userId) {
        return channelService.findAllByUserId(userId);
    }


    @RequestMapping(method = RequestMethod.PUT)
    public ChannelDto update(@RequestBody ChannelUpdateRequest request) {
        return channelService.update(request);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        channelService.delete(id);
    }
}