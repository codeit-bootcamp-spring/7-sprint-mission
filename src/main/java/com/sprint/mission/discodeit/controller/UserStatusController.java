package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.basic.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserStatusController extends BaseController {
    private final UserStatusService userStatusService;

}
