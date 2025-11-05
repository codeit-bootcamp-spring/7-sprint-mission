package com.sprint.mission.discodeit.entity.content;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.entity.common.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class BinaryContent extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    //관련된 유저
    // private final UUID userId;
    //  private final  UUID messageId;
    //하나로 묶는것이 좋지않을까

    private final ContentsType contentsType;
    private final byte[] contentByte;
    private String bytes;

    public BinaryContent(ContentsType contentsType, byte[] contentByte,String bytes) {
        this.contentsType = contentsType;
        this.contentByte = contentByte;
        this.bytes = bytes;
    }

 /*   public BinaryContent(ContentsType contentsType, byte[] contentByte) {
        this.contentsType = contentsType;
        this.contentByte = contentByte;

    }
*/
}
