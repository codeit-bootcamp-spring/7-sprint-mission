package com.sprint.mission.discodeit.loggin;

public interface ArgSanitizer {

  boolean isFilterCase(Object arg);

  Object sanitize(Object arg);
}
