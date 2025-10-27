package com.sprint.mission.discodeit.entity.content;

import com.sprint.mission.discodeit.entity.common.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BinaryContent extends Common {
    //관련된 유저
   private final UUID userId;
   private final  UUID messageId;
    private final  ContentsType contentsType;
    private final  String contentsId;
}
