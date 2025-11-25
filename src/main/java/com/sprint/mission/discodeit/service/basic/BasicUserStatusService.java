package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional // user랑 1:1관계인데, 이 메서드가 필요할까..? 흠
  public UserStatusResponseDto createUserStatus(CreateUserStatusRequestDto requestDto) {
    UUID userId = requestDto.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

    UserStatus newStatus = new UserStatus(user);
    UserStatus userStatus = userStatusRepository.save(newStatus);

    return UserStatusResponseDto.from(userStatus);
  }

  @Override
  public UserStatusResponseDto find(UUID userStatusId) {
    UserStatus userStatus = getUserStatus(userStatusId);
    return UserStatusResponseDto.from(userStatus);
  }

  @Override
  public List<UserStatusResponseDto> findAll() {
    List<UserStatus> userStatuses = userStatusRepository.findAll();
    return userStatuses.stream()
        .map(userStatus -> UserStatusResponseDto.from(userStatus))
        .toList();
  }

  @Override
  @Transactional
  public UserStatus updateUserStatus(UUID id) {
    UserStatus userStatus = userStatusRepository.findByUserId(id)
        .orElseThrow(() -> new IllegalArgumentException("유저 상태를 찾을 수 없습니다."));
    userStatus.statusUpdate(Instant.now());
    return userStatus;
  }

  @Override
  @Transactional
  public UserStatus updateByUserId(UUID userId) {
    UserStatus user = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
    user.statusUpdate(Instant.now());
    return user;
  }

  @Override
  @Transactional
  public void deleteUserStatus(UUID userStatusId) {
    UserStatus userStatus = getUserStatus(userStatusId);
    userStatusRepository.delete(userStatus);
  }

  // 중복 메서드 만들기
  private UserStatus getUserStatus(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new IllegalArgumentException("유저 상태를 찾을 수 없습니다."));
  }

}
