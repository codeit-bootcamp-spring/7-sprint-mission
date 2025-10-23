package com.sprint.mission.discodeit.readstatus.application;

import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.server.domain.Server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

    void save(ReadStatus readStatus);

    void remove(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAll();
}
