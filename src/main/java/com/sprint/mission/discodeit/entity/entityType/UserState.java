package com.sprint.mission.discodeit.entity.entityType;

import lombok.Getter;

@Getter
public enum UserState {
    ONLINE("온라인"), AFK("자리비움"),
    DND("방해금지"), OFFLINE("오프라인");

    private final String descState;

    UserState(String description) {
        this.descState = description;
    }

}
