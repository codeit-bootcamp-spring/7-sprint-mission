package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusGetByUserRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponse>> getByUserId(@RequestParam UUID userId) {
        return ResponseEntity.ok(readStatusService.getAllByUserId(userId));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponse>> getAll() {
        return ResponseEntity.ok(readStatusService.getAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponse> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatusResponse> read(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequest request) {

        return ResponseEntity.ok(readStatusService.update(readStatusId, request));
    }


}
