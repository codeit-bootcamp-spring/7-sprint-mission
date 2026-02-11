package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.binarycontent.FileSaveFailException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage storage;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UserResponseDto createUser(CreateUserRequestDto request, MultipartFile profile)
      throws IOException {
    log.debug("유저 생성 시작 - 유저이름: {}, 이메일: {}", request.username(), request.email());
    // username, email 중복 체크
    if (userRepository.findByUsername(request.username()).isPresent()) {
      log.warn("유저 생성 실패 - 중복된 유저 이름: {}", request.username());
      throw new DuplicateUsernameException(request.username());
    }
    if (userRepository.findByEmail(request.email()).isPresent()) {
      log.warn("유저 생성 실패 - 중복된 이메일: {}", request.email());
      throw new DuplicateEmailException(request.email());
    }

    // 프로필 이미지 선택적 로직, ID로 체크해서. 컨텐츠 만들어서
    BinaryContent saveProfile = null;
    if (profile != null) {
      log.debug("프로필 이미지 등록 - 파일명: {}, 크기: {}",
          profile.getOriginalFilename(), profile.getSize());
      BinaryContent content = new BinaryContent(
          profile.getOriginalFilename(),
          profile.getSize(),
          profile.getContentType()
      );
      saveProfile = binaryContentRepository.save(content);
      storage.put(saveProfile.getId(), profile.getBytes());
      log.info("프로필 이미지 저장 완료 - binaryContent ID: {}", saveProfile.getId());
    }

    // User 생성
    User user = new User(
        request.username(),
        request.email(),
        passwordEncoder.encode(request.password()),
        saveProfile
    );

    UserStatus status = new UserStatus(user);
    user.assignStatus(status);

    User saveUser = userRepository.save(user);
    log.info("유저 생성 완료 - 유저 ID: {}, 유저 이름: {}",
        saveUser.getId(), saveUser.getUsername());
    return userMapper.toDto(saveUser);
  }

  @Override
  public UserResponseDto find(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
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
    log.debug("유저 수정 시작 - 유저 ID: {}", userId);
    //유저 찾기
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("수정 실패 - 존재하지 않은 유저 ID: {}", userId);
          return new UserNotFoundException(userId);
        });

    // 프로필 이미지 선택적 로직
    if (profile != null && !profile.isEmpty()) {
      log.debug("프로필 이미지 등록 - 파일명: {}, 크기: {}",
          profile.getOriginalFilename(), profile.getSize());
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
        log.error("이미지 등록 실패 - 파일명: {}, 크기: {}",
            profile.getOriginalFilename(), profile.getSize());
        throw new FileSaveFailException();
      }
    }

    user.updateInfo(
        request.newUsername(),
        request.newEmail(),
        request.newPassword()
    );
    log.info("유저 수정 완료 - 유저 ID: {}", userId);
    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void deleteUser(UUID userId) {
    log.debug("유저 삭제 시작 - 유저 ID: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("삭제 실패 - 존재하지 않는 유저 ID: {}", userId);
          return new UserNotFoundException(userId);
        });
    if (user.getProfile() != null) {
      log.debug("프로필 이미지 삭제 - binaryContent ID: {}", user.getProfile().getId());
      binaryContentRepository.delete(user.getProfile());
    }
    log.info("유저 삭제 완료 - 유저 ID: {}, 유저 이름: {}", userId, user.getUsername());
    userRepository.delete(user);
  }
}
