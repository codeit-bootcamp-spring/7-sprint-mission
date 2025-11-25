package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
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

    private final ReadStatusService readStatusService;

    @GetMapping
    public List<ReadStatusDto> getReadStatus(@RequestParam UUID userId) {
        return readStatusService.getAllByUserId(userId);
    }

    @PatchMapping("/{id}")
    public ReadStatusDto readChannel(@PathVariable UUID id) {
        return readStatusService.updateReadStatus(id);

    }

    @PostMapping
    public ReadStatusDto createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        return readStatusService.createReadStatus(request);
    }
}
