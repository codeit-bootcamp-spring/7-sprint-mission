package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ReadStatusResponseDto create(
            @Valid @RequestBody ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        return readStatusService.create(readStatusCreateRequestDto);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    public ReadStatusResponseDto update(@PathVariable("readStatusId") UUID readStatusId,
                                        @Valid @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        return readStatusService.update(readStatusId ,readStatusUpdateRequestDto);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.GET)
    public ReadStatusResponseDto get(@PathVariable("readStatusId") UUID readStatusId) {
        return readStatusService.get(readStatusId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ReadStatusResponseDto> getAllByUserId(@RequestParam("userId") UUID userId) {
        return readStatusService.getAllByUserId(userId);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(("readStatusId")) UUID readStatusId) {
        readStatusService.delete(readStatusId);
    }
}
