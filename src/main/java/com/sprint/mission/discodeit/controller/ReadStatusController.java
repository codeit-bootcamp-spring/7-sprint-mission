package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reads")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // 생성은 채널서비스를 할 때마다 생성되고 수정되니까 필요없는 부분인가?
//    @RequestMapping(method = RequestMethod.POST)
//    public ResponseEntity<ReadStatusResponseDto> readStatusCreate(@RequestBody ReadStatusRequestDto readStatusRequestDto) {
//        ReadStatusResponseDto dto = readStatusService.createReadStatus(readStatusRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
//    }

    // 수신 정보 수정
    @RequestMapping(method = RequestMethod.PUT)
    public ReadStatusResponseDto readStatusUpdate(@RequestBody ReadStatusUpdateDto readStatusUpdateDto) {
        return readStatusService.updateReadStatus(readStatusUpdateDto);
    }

    // 사용자의 메시지 수신 정보 조회
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public List<ReadStatusResponseDto> getAllByUserId(@PathVariable UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}

/*
메시지 수신 정보 관리
[ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
[ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
[ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
 */