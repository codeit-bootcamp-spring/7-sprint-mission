package com.sprint.mission.discodeit.entity.content;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.entity.common.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class BinaryContent extends Common {
    //관련된 유저
    // private final UUID userId;
    //  private final  UUID messageId;
    //하나로 묶는것이 좋지않을까
    private final UUID typeUUID;
    private final ContentsType contentsType;
    private final String contentsId;

    public BinaryContent(UUID typeUUID,ContentsType contentsType,String contentsId) {
        this.typeUUID = typeUUID;
        this.contentsType = contentsType;
        this.contentsId = contentsId;
    }
}
