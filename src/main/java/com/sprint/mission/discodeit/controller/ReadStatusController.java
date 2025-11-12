package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BasicReadStatusService;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

    private final BasicReadStatusService readStatusService;

    @GetMapping
    public List<ReadStatusResponse> getReadStatus(@RequestParam UUID userId){
        return readStatusService.getAllByUserId(userId);
    }

    @PatchMapping("/{id}")
    public ReadStatusResponse readChannel(@PathVariable UUID id) {
        return readStatusService.updateReadStatus(id);

    }

    @PostMapping
    public ReadStatusResponse createReadStatus(@RequestBody ReadStatusCreateRequest request){
        return readStatusService.createReadStatus(request);
    }
}
