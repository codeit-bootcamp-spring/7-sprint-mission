package com.sprint.mission.discodeit.event;

import org.springframework.context.ApplicationEvent;

public class UserCacheEvictEvent extends ApplicationEvent {
    public UserCacheEvictEvent(Object source) {
        super(source);
    }
}
