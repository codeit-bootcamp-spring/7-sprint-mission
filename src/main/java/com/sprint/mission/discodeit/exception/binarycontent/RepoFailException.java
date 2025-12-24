package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class RepoFailException extends BinaryContentException {

  public RepoFailException() {
    super(ErrorCode.REPO_FAIL);
  }

}
