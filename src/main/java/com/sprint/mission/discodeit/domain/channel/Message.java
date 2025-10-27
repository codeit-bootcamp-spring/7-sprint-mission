package com.sprint.mission.discodeit.domain.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message {

    private static final long serialVersionUID = 9L;

    private final UUID senderId;
    private final String content;
    private final UUID binaryContentId;


}
