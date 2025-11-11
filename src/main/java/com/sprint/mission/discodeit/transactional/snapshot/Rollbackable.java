package com.sprint.mission.discodeit.transactional.snapshot;

public interface Rollbackable {
    void restore();
}
