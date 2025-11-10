package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ReadStatusResponseDto createReadStatus(@RequestParam UUID userId, @RequestParam UUID channelId){
        return readStatusService.createReadStatus(
                new ReadStatusCreateRequestDto(userId,channelId, Instant.now())
        );
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public <T>void updateReadStatus(@RequestBody ReadStatusUpdateRequestDto<T> dto){
        readStatusService.updateReadStatus(dto);
    }

    @RequestMapping(value = "/readByUserId", method = RequestMethod.GET)
    public List<ReadStatusResponseDto> readReadStatus(@RequestParam UUID userId){
        return readStatusService.findAllyByUserId(userId);
    }

    @RequestMapping("/readAll")
    public List<ReadStatusResponseDto> readAll(){
        return readStatusService.readAllReadStatus();
    }
}
