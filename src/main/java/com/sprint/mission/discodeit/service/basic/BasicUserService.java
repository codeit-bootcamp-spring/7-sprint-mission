package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateUserCommand;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User createUser(CreateUserCommand request) {
    // username, email 중복 체크
    if (userRepository.findByUsername(request.username()).isPresent()) {
      throw new IllegalArgumentException("이미 유저 이름이 있습니다.");
    }
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new IllegalArgumentException("이미 이메일이 있습니다.");
    }

    // 프로필 이미지 선택적 로직, ID로 체크해서. 컨텐츠 만들어서
    UUID profileId = null;
    if (request.bytes() != null) {
      BinaryContent content = new BinaryContent(
          request.fileName(),
          request.contentType(),
          request.bytes()
      );

      BinaryContent saved = binaryContentRepository.save(content);

      profileId = saved.getId();
    }

    // User 생성
    User user = new User(
        request.username(),
        request.email(),
        request.password(),
        profileId
    );

    User userCreated = userRepository.save(user);

    // UserStatus 생성
    UserStatus status = new UserStatus(userCreated.getId());
    userStatusRepository.save(status);
    return userCreated;
  }

  @Override
  public UserResponseDto find(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

    UserStatus status = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("유저 상태를 찾을 수 없습니다."));

    return UserResponseDto.from(user, status);
  }

  @Override
  public List<UserResponseDto> findAll() {
    List<User> users = userRepository.findAll();
    List<UserResponseDto> dtos = new ArrayList<>();
    for (User user : users) {
      UserStatus status = userStatusRepository.findByUserId(user.getId())
          .orElseGet(() -> {
            UserStatus userStatus = new UserStatus(user.getId());
            return userStatusRepository.save(userStatus);
          });

      UserResponseDto dto = UserResponseDto.from(user, status);
      dtos.add(dto);
    }
    return dtos;
  }

  @Override
  public User updateUser(UUID userId, UpdateUserDto request, MultipartFile profile) {
    //유저 찾기
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

    // 프로필 이미지 선택적 로직
    if (profile != null && !profile.isEmpty()) {
      try {
        BinaryContent content = new BinaryContent(
            profile.getOriginalFilename(),
            profile.getContentType(),
            profile.getBytes()
        );

        BinaryContent saved = binaryContentRepository.save(content);
        user.updateProfile(saved.getId());
      } catch (IOException e) {
        throw new IllegalArgumentException("이미지 등록 실패", e);
      }
    }

    user.updateInfo(
        request.newUsername(),
        request.newEmail(),
        request.newPassword()
    );

    User updated = userRepository.save(user);

    UserStatus status = userStatusRepository.findByUserId(updated.getId())
        .orElseThrow(() -> new IllegalArgumentException("유저 상태를 찾을 수 없습니다."));
    userStatusRepository.save(status);

    return updated;
  }

  @Override
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    UUID profileId = user.getProfileId();
    binaryContentRepository.delete(profileId);
    userStatusRepository.deleteByUserId(user.getId());
    userRepository.delete(user.getId());
  }
}
