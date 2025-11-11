package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ChannelResponseDto createPublic(
            @Valid @RequestBody PublicChannelCreateRequestDto requestDto) {
        Channel channel = channelService.createPublic(requestDto);
        return ChannelResponseDto.from(channel, null);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ChannelResponseDto createPrivate(
            @Valid @RequestBody PrivateChannelCreateRequestDto requestDto) {
        Channel channel = channelService.createPrivate(requestDto);
        return ChannelResponseDto.from(channel, null);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ChannelResponseDto update(@Valid @RequestBody ChannelUpdateRequestDto requestDto,
                                     @PathVariable("id") UUID id) {
        if(requestDto.channelId() == null || !id.equals(requestDto.channelId())){
            throw new IllegalArgumentException("Invalid channel id");
        }
        Channel channel = channelService.update(requestDto);
        return ChannelResponseDto.from(channel, channelService.getLastMessageAt(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ChannelResponseDto get(@PathVariable UUID id) {
        Channel channel = channelService.get(id);
        Instant lastMesasge = channelService.getLastMessageAt(id);
        return ChannelResponseDto.from(channel, lastMesasge);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        channelService.delete(id);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelResponseDto> getAllByUserId(
            @RequestParam(name = "usersId", required = false) UUID id) {

        List<Channel> channels = (id == null) ? channelService.getAll() : channelService.getAllByUserId(id);

        return channels.stream()
                .map(channel -> ChannelResponseDto.from(channel,
                        channelService.getLastMessageAt(channel.getId())))
                .toList();
    }

    @RequestMapping(value = "/{id}/join", method = RequestMethod.POST)
    public void join(@PathVariable("id") UUID channelId,
                     @RequestParam("userId") UUID userId) {
        boolean joined = channelService.join(channelId, userId);
        if(!joined){}
    }

    @RequestMapping(value = "{id}/leave", method = RequestMethod.POST)
    public void leave(@PathVariable("id") UUID channelId,
                      @RequestParam("userId") UUID userId) {
        boolean leave = channelService.leave(channelId, userId);
        if(!leave){}
    }
}
