package com.sprint.mission.discodeit.exception.domain.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.nio.file.Path;
import java.util.HashMap;

public class FileReadFailException extends FileException{

    public FileReadFailException(Path storagePath){
        super(ErrorCode.FILE_READ_FAIL,new HashMap<>(){{put("storagePath",storagePath);}});
    }
}
