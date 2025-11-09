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
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponse> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
    }

    @RequestMapping(value = "/read", method = RequestMethod.PUT)
    public ResponseEntity<ReadStatusResponse> read(@Valid @RequestBody ReadStatusUpdateRequest request) {
        return ResponseEntity.ok(readStatusService.update(request));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponse>> getByUserId(@RequestParam UUID id) {
        return ResponseEntity.ok(readStatusService.getAllByUserId(id));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponse>> getAll() {
        return ResponseEntity.ok(readStatusService.getAll());
    }


}
