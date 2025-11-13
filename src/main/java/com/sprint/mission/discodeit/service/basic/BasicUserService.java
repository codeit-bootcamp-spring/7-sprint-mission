package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public void updateStatus(UUID userId, Map<String, Object> statusReq) {
    // TODO: 실제 상태 변경 로직은 UserStatus 구조와 요구사항에 맞게 구현하세요.
    // 현재는 사용자 존재 여부만 검증하는 최소 구현입니다.
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("User with id " + userId + " not found");
      // userId로 사용자가 존재하는지 확인 후 없으면 예외 던지고 (NoSuchElementException,
      // 있으면 아직 아무것도 안 하는 상태임(추가 예정).
    }
  }

  // 유저를 만들고, 필요하면 프로필 파일을 저장하고 상태까지 기본 값을 넣는 작업을 전부 한 번에 하고
  // 마지막에 만들어진 User를 돌려주는 메서드
  @Override
  public User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    // 중복 체크 (이미 있는 이메일이나 유저네임이면 예외 던짐)
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    // 프로필 이미지 저장
    /*
    optionalProfileCreateRequest안에 값이 있으면
      - 파일 이름, 타입, 바이트 배열을 꺼낸다
      - BinaryContent 엔티티를 만들어서 biranyContentRepository.save()로 저장한다.
      - 저장된 파일의 id만 꺼내서 nullableProfileId에 넣는다.
    값이 있으면 nullalbleProfileId는 null로 둔다.
    * */
    UUID nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);
    String password = userCreateRequest.password();

    // Entity 생성 + 저장
    // 이름 이메일 패스워드 프로필이미지
    User user = new User(username, email, password, nullableProfileId);
    // userRepository.save(user) 로 DB 같은 저장소에 저장
    User createdUser = userRepository.save(user);

    // 현재 시간 Instant.now()로
    // UserStatus 엔티티를 만들어서 userRepository.save(user) 로 DB 같은 저장소에 저장
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(createdUser.getId(), now); //
    userStatusRepository.save(userStatus);

    return createdUser;
  }

  @Override
  public UserDto find(UUID userId) {  // id로 유저 찾고 없으면 에러
    return userRepository.findById(userId)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {  // 전체 유저 목록 가져온 후, toDto로 변환
    return userRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  // 유저 찾기 → 중복 체크 → 프로필 바꾸면 이전 파일 삭제 → 새 파일 저장 → 유저 정보 업데이트
  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    UUID nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfileId())
              .ifPresent(binaryContentRepository::deleteById);

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfileId);

    return userRepository.save(user);
  }

  // 유저 찾기 → 프로필 파일 있으면 같이 삭제 → UserStatus 삭제 → 마지막에 유저 삭제
  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    Optional.ofNullable(user.getProfileId())
        .ifPresent(binaryContentRepository::deleteById);
    userStatusRepository.deleteByUserId(userId);

    userRepository.deleteById(userId);
  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        online
    );
  }
}
