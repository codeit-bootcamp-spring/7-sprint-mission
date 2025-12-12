package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileSaveFailException extends BinaryContentException {

  public FileSaveFailException() {
    super(ErrorCode.FILE_SAVE_FAIL);
  }

}
