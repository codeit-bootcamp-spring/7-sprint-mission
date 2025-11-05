package com.sprint.mission.discodeit.event.dispatcher;

import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreateDispatcher {

    private final UserService userService;
}
