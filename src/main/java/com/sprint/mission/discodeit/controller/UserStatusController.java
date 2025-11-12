package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatustUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userStatus")
public class UserStatusController {

    private final UserStatusService userStatusService;

    // [온라인 상태 업데이트]
    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public UserStatusResponse updateStatus(@RequestBody UserStatustUpdateRequest req) {
        return userStatusService.update(req);
    }


}
