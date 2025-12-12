package com.sprint.mission.discodeit.exception.domain.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class FileByteReadFailException extends FileException{
    public FileByteReadFailException(String fileName){
        super(ErrorCode.FILE_BYTE_READ_FAIL,new HashMap<>(){{put("fileName",fileName);}});
    }
}
