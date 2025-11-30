package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ReadStatusDto>> getReadStatus(@RequestParam UUID userId) {
        List<ReadStatusDto> allByUserId = readStatusService.getAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(allByUserId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadStatusDto> readChannel(@PathVariable UUID id) {
        ReadStatusDto readStatusDto = readStatusService.updateReadStatus(id);
        return ResponseEntity.status(HttpStatus.OK).body(readStatusDto);

    }

    @PostMapping
    public ResponseEntity<ReadStatusDto> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        ReadStatusDto readStatusDto = readStatusService.createReadStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatusDto);

    }
}
