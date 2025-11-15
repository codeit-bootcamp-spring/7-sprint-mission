package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @PostMapping
    public ResponseEntity<ReadStatusDto> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus created = readStatusService.create(request);
        return ResponseEntity.ok(ReadStatusDto.from(created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadStatusDto> update(@PathVariable UUID id,
        @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updated = readStatusService.update(id, request);
        return ResponseEntity.ok(ReadStatusDto.from(updated));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatusDto>> findAllByUser(@PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);

        List<ReadStatusDto> response = readStatuses.stream()
                .map(status -> ReadStatusDto.from(status))
                .toList();

        return ResponseEntity.ok(response);
    }
}
