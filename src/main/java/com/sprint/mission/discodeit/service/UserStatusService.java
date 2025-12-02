package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusViewRes;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatus userStatus);

  UserStatus findByUserId(UUID userId);

  UserStatusViewRes findById(UUID id);

  void updateOfflineAt(UUID id);

  void update(UUID id);

  void updateByUserId(UUID userId);

  void delete(UUID id);
}
