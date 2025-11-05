package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusGetByUserRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponse> create(ReadStatusCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ReadStatusResponse> read(ReadStatusUpdateRequest request) {
        return ResponseEntity.ok(readStatusService.update(request));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponse>> getByUserId(@PathVariable String id) {
        return ResponseEntity.ok(readStatusService.getAllByUserId(id));
    }


}
