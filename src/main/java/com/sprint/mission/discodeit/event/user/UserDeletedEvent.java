package com.sprint.mission.discodeit.event.user;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDeletedEvent {

    private final UserResponseDto userResponseDto;
}
