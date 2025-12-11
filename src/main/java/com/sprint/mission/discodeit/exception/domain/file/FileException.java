package com.sprint.mission.discodeit.exception.domain.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class FileException extends DiscodeitException {

    public FileException(ErrorCode errorCode, Map<String, Object> details){
        super(errorCode,details);
    }
}
