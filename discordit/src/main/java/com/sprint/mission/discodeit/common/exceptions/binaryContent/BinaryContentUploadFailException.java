package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.entity.BinaryContent;

public class BinaryContentUploadFailException extends BinaryContentException {
    public BinaryContentUploadFailException(BinaryContent content) {
        super(content.getId(), ErrorCode.UPLOAD_FAILED);
    }
}
