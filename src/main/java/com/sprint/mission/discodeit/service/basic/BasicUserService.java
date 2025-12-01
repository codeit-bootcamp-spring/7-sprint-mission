package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserResponseDto createUser(CreateUserDto createUserDto,
      Optional<CreateBinaryContentDto> createBinaryContentDto) {
    if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
      throw new CustomException(ErrorCode.USERNAME_ALREADY_EXIST);
    }
    if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
      throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
    }
    BinaryContent profile = createBinaryContentDto
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          Long size = profileRequest.size();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, size, contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);

    User user = new User(
        createUserDto.username(), createUserDto.email(), createUserDto.password(), profile);
    userRepository.save(user);

    return userMapper.toResponseDto(user);
  }
  
  @Override
  @Transactional(readOnly = true)
  public UserResponseDto getUser(UUID userId) { // 단건 검색
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return userMapper.toResponseDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserResponseDto> getAllUsers() {
    List<User> users = userRepository.findAllWithProfileAndStatus();

    return users.stream()
        .map(userMapper::toResponseDto)
        .toList();
  }

  @Override
  @Transactional
  public UserResponseDto updateUser(UUID userId, UpdateUserDto updateUserDto,
      Optional<CreateBinaryContentDto> createBinaryContentDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));

    BinaryContent profile = createBinaryContentDto
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfile()).ifPresent(binaryContentRepository::delete);

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          Long size = profileRequest.size();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, size, contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);

    user.updateUser(updateUserDto.newUsername(),
        updateUserDto.newPassword(),
        updateUserDto.newEmail(),
        profile
    );

    userRepository.save(user);
    return userMapper.toResponseDto(user);
  }

  @Override
  @Transactional
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    // User에서 cascadeType.ALL로 자식에게 전파하므로, User가 삭제되면
    // UserStatus도 같이 삭제됨.
    userRepository.deleteById(userId);
  }
}
