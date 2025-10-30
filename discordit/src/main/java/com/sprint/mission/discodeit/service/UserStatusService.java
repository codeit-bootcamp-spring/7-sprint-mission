package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;

    public UserStatusResponse create(UserStatusCreateRequestDto dto) {

    }
}
