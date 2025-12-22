package com.sprint.mission.discodeit.exception.domain.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class FileNotExistException extends FileException{

    public FileNotExistException(UUID fileId){
        super(ErrorCode.FILE_NOT_EXIST,new HashMap<>(){{put("fileId",fileId);}});
    }
}
