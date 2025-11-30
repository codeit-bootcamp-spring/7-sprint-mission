package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateUserCommand;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage storage;

  @Override
  @Transactional
  public UserResponseDto createUser(CreateUserRequestDto request, MultipartFile profile)
      throws IOException {
    // username, email 중복 체크
    if (userRepository.findByUsername(request.username()).isPresent()) {
      throw new IllegalArgumentException("이미 유저 이름이 있습니다.");
    }
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new IllegalArgumentException("이미 이메일이 있습니다.");
    }

    // 프로필 이미지 선택적 로직, ID로 체크해서. 컨텐츠 만들어서
    BinaryContent saveProfile = null;
    if (profile != null) {
      BinaryContent content = new BinaryContent(
          profile.getOriginalFilename(),
          profile.getSize(),
          profile.getContentType()
      );
      saveProfile = binaryContentRepository.save(content);
      storage.put(saveProfile.getId(), profile.getBytes());
    }

    // User 생성
    User user = new User(
        request.username(),
        request.email(),
        request.password(),
        saveProfile
    );

    UserStatus status = new UserStatus(user);
    user.assignStatus(status);

    User saveUser = userRepository.save(user);
    return userMapper.toDto(saveUser);
  }

  @Override
  public UserResponseDto find(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    return userMapper.toDto(user);
  }

  @Override
  public List<UserResponseDto> findAll() {
    return userRepository.findAll().stream()
        .map(user -> userMapper.toDto(user))
        .toList();
  }

  @Override
  @Transactional
  public UserResponseDto updateUser(UUID userId, UpdateUserDto request, MultipartFile profile) {
    //유저 찾기
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

    // 프로필 이미지 선택적 로직
    if (profile != null && !profile.isEmpty()) {
      try {
        BinaryContent content = new BinaryContent(
            profile.getOriginalFilename(),
            profile.getSize(),
            profile.getContentType()
        );

        BinaryContent saved = binaryContentRepository.save(content);
        user.updateProfile(saved);
        storage.put(saved.getId(), profile.getBytes());
      } catch (IOException e) {
        throw new IllegalArgumentException("이미지 등록 실패", e);
      }
    }

    user.updateInfo(
        request.newUsername(),
        request.newEmail(),
        request.newPassword()
    );
    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    if (user.getProfile() != null) {
      binaryContentRepository.delete(user.getProfile());
    }
    userRepository.delete(user);
  }
}
