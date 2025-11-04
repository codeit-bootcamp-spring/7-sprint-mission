package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/readstatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createReadStatus(@RequestBody CreateReadStatusRequestDto requestDto) {
        try {
            readStatusService.create(requestDto);
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Read Status Created");
    }

    // 특정 채널의 메시지 수신 정보 수정
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<String> createReadStatus(@RequestBody UpdateReadStatusRequestDto requestDto) {
        try {
            readStatusService.update(requestDto);
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Read Status Updated");
    }

    // 특정 사용자의 메시지 수신 정보 조회
    @RequestMapping(value = "/search/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> search(@PathVariable UUID userId) {
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);

        return ResponseEntity.ok(readStatusList);
    }
}
