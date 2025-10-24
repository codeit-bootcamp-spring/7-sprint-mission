package com.sprint.mission.discodeit.message.domain;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
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
