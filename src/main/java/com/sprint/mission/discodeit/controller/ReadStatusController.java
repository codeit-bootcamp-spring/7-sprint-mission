package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.dto.ApiResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReadStatusController implements ReadStatusControllerDocs {
    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(value = "/readStatuses", method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusRequestDto requestDto) {
        ReadStatus createdReadStatus = readStatusService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
    }

    // 특정 채널의 메시지 수신 정보 수정
    @RequestMapping(value = "/readStatuses/{readStatusId}", method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatus> createReadStatus(@PathVariable UUID readStatusId, @RequestBody UpdateReadStatusRequestDto requestDto) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }

    // 특정 사용자의 메시지 수신 정보 조회
    @RequestMapping(value = "/readStatuses", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> search(@RequestParam UUID userId) {
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }
}
