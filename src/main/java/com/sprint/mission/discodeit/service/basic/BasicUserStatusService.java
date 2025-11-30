package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusViewRes;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;

  // ===== 🏗️ Domain Logic (Facade 용)  =====
  @Override
  public UserStatus create(UserStatus userStatus) {
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUser_Id(userId).orElseThrow(
        () -> new CustomException(ErrorCode.USER_STATUS_MISSING)
    );
  }

  // ===== 🎯 Controller Direct (DTO 반환) =====
  @Override
  public UserStatusViewRes findById(@NonNull UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.USERSTATUS_NOT_FOUND)
    );
    return UserStatusViewRes.from(userStatus);
  }

  // ===== 🔧 Controller Direct (단일 도메인 / void) =====
  @Override
  @Transactional
  public void updateOfflineAt(@NonNull UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.USERSTATUS_NOT_FOUND)
    );
    userStatus.updateOfflineAt();
  }

  @Override
  @Transactional
  public void update(@NonNull UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.USERSTATUS_NOT_FOUND)
    );
    userStatus.update();
  }

  @Override
  @Transactional
  public void updateByUserId(@NonNull UUID userId) {
    UserStatus userStatus = findByUserId(userId);
    findByUserId(userId).update();
  }

  @Override
  public void delete(@NonNull UUID id) {
    userStatusRepository.deleteById(id);
  }
}
