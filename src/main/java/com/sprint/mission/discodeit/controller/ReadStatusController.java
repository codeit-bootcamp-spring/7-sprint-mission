package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Docs.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindByUserRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusControllerDocs {

    private final ReadStatusService readStatusService;

    //[ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createStatus(@RequestBody ReadStatusCreateRequest request){
        ReadStatus readStatus = readStatusService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readStatus);
    }
    // [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.


    @RequestMapping(path = "{readStatusId}",method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatus> updateStatus(@PathVariable UUID readStatusId,
                                                   @RequestBody ReadStatusUpdateReuqest request){
        ReadStatus update = readStatusService.update(readStatusId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(update);
    }
    //  [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> findStatusByUserId(@RequestParam("userId") UUID userId){
        List<ReadStatus> allByUserId = readStatusService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allByUserId);
    }
}