package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> getByUserId(@RequestParam UUID userId) {
        return ResponseEntity.ok(readStatusService.getAllByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<ReadStatusDto> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
    }

    @PatchMapping(value = "/{readStatusId}")
    public ResponseEntity<ReadStatusDto> read(@PathVariable UUID readStatusId, @Valid @RequestBody ReadStatusUpdateRequest request) {
        return ResponseEntity.ok(readStatusService.update(readStatusId, request));
    }


}
