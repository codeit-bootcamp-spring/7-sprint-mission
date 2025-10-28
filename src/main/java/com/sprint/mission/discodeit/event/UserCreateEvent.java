package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreateEvent extends ApplicationEvent {

    private final ProfileCreateRequestDto profileCreateRequestDto;
    public UserCreateEvent(Object source, ProfileCreateRequestDto profileCreateRequestDto) {
        super(source);
        this.profileCreateRequestDto = profileCreateRequestDto;
    }
}
