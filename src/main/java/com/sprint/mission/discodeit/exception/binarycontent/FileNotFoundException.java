package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileNotFoundException extends BinaryContentException {

  public FileNotFoundException() {
    super(ErrorCode.FILE_NOT_FOUND);
  }

}
