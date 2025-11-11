package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.readstatus.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/read-status")
public class ReadStatusController {


    private final ReadStatusService readStatusService;


    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }


    @RequestMapping(method = RequestMethod.POST)
    public UUID create(@RequestBody ReadStatusCreateRequest request) {
        return readStatusService.create(request);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public List<?> findAllByUser(@PathVariable UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }


    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody ReadStatusUpdateRequest request) {
        readStatusService.update(request);
    }
}