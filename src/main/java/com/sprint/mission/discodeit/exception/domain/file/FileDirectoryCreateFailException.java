package com.sprint.mission.discodeit.exception.domain.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.nio.file.Path;
import java.util.HashMap;

public class FileDirectoryCreateFailException extends FileException {
    public FileDirectoryCreateFailException(Path storagePath){
        super(ErrorCode.FILE_DIRECTORY_CREATE_FAIL,new HashMap<>(){{put("storagePath",storagePath);}});
    }
}
