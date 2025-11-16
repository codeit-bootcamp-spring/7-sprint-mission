package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.response.AvailabilityRes;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

  User create(User user);

  List<User> findAll();

  User findByEmail(String email);

  User findByNickname(String nickname);

  User findById(UUID id);

  void delete(UUID id);

  void update(UUID id, UserUpdateReq req);

  void updateProfileImage(UUID id, UUID profileId);

  AvailabilityRes isRegisteredNickname(String nickname);

  AvailabilityRes isRegisteredEmail(String email);
}
