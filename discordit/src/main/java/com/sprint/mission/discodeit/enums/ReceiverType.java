package com.sprint.mission.discodeit.enums;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiverType {
    USER(User.class),
    CHANNEL(Channel.class);

    private final Class<? extends Receivable> type;

    public static ReceiverType from(Receivable receivable) {
        if (receivable instanceof User) {
            return USER;
        }
        if (receivable instanceof Channel) {
            return CHANNEL;
        }
        throw new IllegalArgumentException("지원하지 않는 수신자 타입입니다: " + receivable.getClass().getName());
    }
}
