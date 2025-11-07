package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.response.ApiResponse;
import com.sprint.mission.discodeit.dto.readstatus.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.facade.readstatus.ReadStatusCreateFacade;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusCreateFacade readStatusCreateFacade;
    private final ReadStatusService readStatusService;

    //메세지 수신 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Void>> createReadStatus(@RequestBody ReadStatusCreateReq req){
        ReadStatus readStatus = readStatusCreateFacade.create(req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(readStatus.getId())
                .toUri();
        return ResponseEntity.created(location).body(ApiResponse.success());
    }
    
    //메세지 수신 정보 업데이트
    @RequestMapping(method = RequestMethod.PATCH, value = "/{readStatusId}")
    public ResponseEntity<ApiResponse<Void>> updateReadStatus(@PathVariable UUID readStatusId){
        readStatusService.update(readStatusId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    //메세지 수신 정보 조회
    @RequestMapping(method = RequestMethod.GET, value = "/{readStatusId}")
    public ResponseEntity<ApiResponse<ReadStatusInfoRes>> getReadStatus(@PathVariable UUID readStatusId){
        return ResponseEntity.ok(ApiResponse.success(readStatusService.findById(readStatusId)));
    }
}
