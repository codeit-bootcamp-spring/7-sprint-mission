package com.sprint.mission.discodeit.controller;

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
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(value = "/readstatus", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Object>> createReadStatus(@RequestBody CreateReadStatusRequestDto requestDto) {
        readStatusService.create(requestDto);
        return ApiResponse.success(HttpStatus.CREATED, "read status가 생성되었습니다.");
    }

    // 특정 채널의 메시지 수신 정보 수정
    @RequestMapping(value = "/readstatus", method = RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<Object>> createReadStatus(@RequestBody UpdateReadStatusRequestDto requestDto) {
        readStatusService.update(requestDto);
        return ApiResponse.success(HttpStatus.OK, "read status가 수정되었습니다.");
    }

    // 특정 사용자의 메시지 수신 정보 조회
    @RequestMapping(value = "/readstatus", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ReadStatus>>> search(@RequestParam UUID userId) {
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
        return ApiResponse.success(HttpStatus.OK, "사용자 메시지 수신 정보 조회", readStatusList);
    }
}
