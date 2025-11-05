package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * [ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.  /api/read-status, POST
 * [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.  /api/read-status/{readStatusId}, PUT
 * [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다. /api/read-status?userId=, GET
 */
@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createReadStatus(@RequestBody CreateReadStatusDto createReadStatusDto) {
        ReadStatusResponseDto readStatusResponseDto = readStatusService.createReadStatus(createReadStatusDto);
        return ResponseEntity.ok(readStatusResponseDto);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateReadStatus(@PathVariable UUID readStatusId, @RequestBody UpdateReadStatusDto updateReadStatusDto) {
        ReadStatusResponseDto readStatusResponseDto = readStatusService.updateReadStatus(readStatusId, updateReadStatusDto);

        return ResponseEntity.ok(readStatusResponseDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDto>> getAllReadStatus(@RequestParam UUID userId) {
        List<ReadStatusResponseDto> list = readStatusService.getAllReadStatusByUserId(userId);
        return  ResponseEntity.ok(list);

    }
}
