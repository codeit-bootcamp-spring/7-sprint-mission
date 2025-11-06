package com.sprint.mission.discodeit.controller;

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
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusCreateFacade readStatusCreateFacade;
    private final ReadStatusService readStatusService;

    //메세지 수신 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createReadStatus(@RequestBody ReadStatusCreateReq req){
        ReadStatus readStatus = readStatusCreateFacade.create(req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(readStatus.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    //메세지 수신 정보 업데이트
    @RequestMapping(method = RequestMethod.PATCH, value = "/{readStatusId}")
    public ResponseEntity<Void> updateReadStatus(@PathVariable UUID readStatusId){
        readStatusService.update(readStatusId);
        return ResponseEntity.noContent().build();
    }

    //메세지 수신 정보 조회
    @RequestMapping(method = RequestMethod.GET, value = "/{readStatusId}")
    public ResponseEntity<ReadStatusInfoRes> getReadStatus(@PathVariable UUID readStatusId){
        return ResponseEntity.ok(readStatusService.findById(readStatusId));
    }
}
