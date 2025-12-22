package com.sprint.mission.discodeit.exception.domain.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.nio.file.Path;
import java.util.HashMap;

public class FileWriteFailException extends FileException{
    public FileWriteFailException(Path storagePath){
        super(ErrorCode.FILE_WRITE_FAIL,new HashMap<>(){{put("storagePath",storagePath);}});
    }
}
