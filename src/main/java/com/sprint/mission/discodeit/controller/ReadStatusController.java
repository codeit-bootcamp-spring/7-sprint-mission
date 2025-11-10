package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicReadStatusService;
import com.sprint.mission.discodeit.application.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.application.dto.response.ReadStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

    private final BasicReadStatusService readStatusService;

    @PostMapping("/create")
    public String makeReadStatus(@ModelAttribute ReadStatusCreateRequest request){
        readStatusService.createReadStatus(request.userId(),request.channelId());
        return "메시지 수신 정보 생성 성공";
    }

    @GetMapping("/{id}")
    public long checkLastTime(@PathVariable UUID id){
        return readStatusService.getTimeSinceLastRead(id);
    }

    @GetMapping("/read/{id}")
    public String readChannel(@PathVariable UUID id) {
        readStatusService.updateReadStatus(id);
        return "읽음";
    }

    @PostMapping("/")
    public ReadStatusResponse getReadStatusByIds(@RequestBody ReadStatusRequest request){
        return readStatusService.getReadStatusByIds(request);
    }
}
