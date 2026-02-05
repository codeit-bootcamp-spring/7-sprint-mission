package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelResponseDto createPublic(
            @Valid @RequestBody PublicChannelCreateRequestDto requestDto) {
        return channelService.createPublic(requestDto);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelResponseDto createPrivate(
            @Valid @RequestBody PrivateChannelCreateRequestDto requestDto) {
        return channelService.createPrivate(requestDto);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
    public ChannelResponseDto update(@Valid @RequestBody ChannelUpdateRequestDto requestDto,
                                     @PathVariable("channelId") UUID channelId) {
        return channelService.update(channelId, requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelResponseDto> getAllByUserId(
            @RequestParam(name = "userId") UUID userId) {
        return channelService.getAllByUserId(userId);
    }
}
