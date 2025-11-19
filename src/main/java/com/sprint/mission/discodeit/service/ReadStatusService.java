package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus create(ReadStatus readStatus);

  ReadStatus update(UUID id);

  void delete(UUID id);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatusInfoRes findById(UUID id);
}
