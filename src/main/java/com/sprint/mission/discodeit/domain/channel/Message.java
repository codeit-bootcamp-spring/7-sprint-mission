package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.binarycontent.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message implements Serializable {

    private final UUID senderId;
    private final String content;
    private final BinaryContent binaryContentId;
    private final Instant createdAt;


}
