package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class GetFileFailException extends BinaryContentException {

  public GetFileFailException() {
    super(ErrorCode.GET_FILE_FAIL);
  }

}
