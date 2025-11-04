package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindByUserRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/readstatus")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    //[ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
    @PostMapping("/create")
    public ReadStatusResponse createStatus(@RequestBody ReadStatusCreateRequest request){
        return readStatusService.create(request);
    }
    // [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.

    @PostMapping("/update")
    public ReadStatusResponse updateStatus(@RequestBody ReadStatusUpdateReuqest request){
        return readStatusService.update(request);
    }
    //  [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
    @PostMapping("/findbyuserid")
    public List<ReadStatusResponse> findStatusByUserId(@RequestBody ReadStatusFindByUserRequest request){
        return readStatusService.findAllByUserId(request);
    }
}