package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.enums.Status;
import com.sprint.mission.discodeit.user.state.dto.UserStatusResponse;
import com.sprint.mission.discodeit.user.state.dto.UserStatusResponseDTO;
import com.sprint.mission.discodeit.user.state.dto.UserStatusUpdateRequest;
import java.time.Instant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserStatusServiceImpl extends
    BaseServiceImpl<UserStatus, UUID, UserStatusRepository> implements UserStatusService {

  public UserStatusServiceImpl(UserStatusRepository repository) {
    super(repository);
  }

  @Override
  public UserStatusResponseDTO create(UUID userId) {
    return UserStatusResponseDTO.fromEntity(save(UserStatus.create(userId)));
  }

  @Override
  public UserStatusResponse updateLastOnline(UUID userId, UserStatusUpdateRequest requestDTO) {

    return UserStatusResponse.fromEntity(findByUserIdHelper(userId).updateLastOnlineAt(
        Instant.parse(requestDTO.newLastActiveAt())));
  }

  @Override
  public UserStatusResponseDTO toAway(UUID userId) {
    UserStatus status = findByUserIdHelper(userId);
    status.setAway();
    return UserStatusResponseDTO.fromEntity(save(status));
  }

  @Override
  public UserStatusResponseDTO toOffline(UUID userId) {
    UserStatus status = findByUserIdHelper(userId);
    status.setOffline();
    return UserStatusResponseDTO.fromEntity(save(status));
  }

  @Override
  public UserStatusResponseDTO toOnline(UUID userId) {
    UserStatus status = findByUserIdHelper(userId);
    status.setOnline();
    return UserStatusResponseDTO.fromEntity(save(status));
  }

  @Override
  public UserStatusResponseDTO toDoNotDisturb(UUID userId, String message) {
    UserStatus status = findByUserIdHelper(userId);
    status.setDoNotDisturb(message);
    return UserStatusResponseDTO.fromEntity(save(status));
  }

  @Override
  public UserStatusResponseDTO findByUserId(UUID userId) {
    return UserStatusResponseDTO.fromEntity(findByUserIdHelper(userId));
  }

  private UserStatus findByUserIdHelper(UUID userId) {
    return repository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("사용자의 상태 정보를 가져올 수 없습니다."));
  }

  @Override
  public void deleteByUserId(UUID userId) {
    repository.deleteById(findByUserIdHelper(userId).getId());
  }

  @Override
  public boolean existsByUserId(UUID userId) {
    return repository.existsByUserId(userId);
  }

  @Override
  public List<UserStatusResponseDTO> findAllByState(Status currentStatus) {
    return repository.findAllByState(currentStatus).stream()
        .map(UserStatusResponseDTO::fromEntity)
        .toList();
  }
}
