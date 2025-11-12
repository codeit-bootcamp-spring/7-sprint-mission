package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.util.exception.CustomException;
import com.sprint.mission.discodeit.global.util.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public UserResponseDto createUser(CreateUserDto createUserDto,
      CreateBinaryContentDto createBinaryContentDto) {
    if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
      throw new CustomException(ErrorCode.USERNAME_ALREADY_EXIST);
    }
    if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
      throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
    }

    UUID profileId = null;

    if (createBinaryContentDto != null) {
      BinaryContent binaryContent = new BinaryContent(
          createBinaryContentDto.fileName(),
          createBinaryContentDto.size(),
          createBinaryContentDto.contentType(),
          createBinaryContentDto.bytes()
      );
      profileId = binaryContent.getId();
      binaryContentRepository.save(binaryContent);
    }

    User user = new User(
        createUserDto.username(), createUserDto.email(), createUserDto.password(),
        createUserDto.phoneNumber(), createUserDto.pronoun(), profileId);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());

    userRepository.save(user);
    userStatusRepository.save(userStatus);

    return UserResponseDto.from(user, userStatus.isOnline());
  }

  @Override
  public UserResponseDto getUser(UUID userId) { // 단건 검색
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));

    return UserResponseDto.from(user, userStatus.isOnline());
  }

  @Override
  public List<UserResponseDto> getAllUsers() {
    List<User> users = userRepository.findAll();

    Map<UUID, UserStatus> statusMap = userStatusRepository.findAll().stream()
        .collect(Collectors.toMap(UserStatus::getUserId, s -> s));

    return users.stream()
        .map(user -> {
          UserStatus status = statusMap.get(user.getId());
          return UserResponseDto.from(user, status.isOnline());
        })
        .toList();
  }

  @Override
  public UserResponseDto updateUser(UUID userId, UpdateUserDto updateUserDto,
      CreateBinaryContentDto createBinaryContentDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));

    UUID profileId = null;

    if (createBinaryContentDto != null) {
      BinaryContent binaryContent = new BinaryContent(
          createBinaryContentDto.fileName(),
          createBinaryContentDto.size(),
          createBinaryContentDto.contentType(),
          createBinaryContentDto.bytes()
      );
      profileId = binaryContent.getId();
      binaryContentRepository.save(binaryContent);
    }

    user.updateUser(updateUserDto.username(),
        updateUserDto.password(),
        updateUserDto.email(),
        updateUserDto.phoneNumber(),
        updateUserDto.pronoun(),
        profileId
    );

    userRepository.save(user);
    return UserResponseDto.from(user, userStatus.isOnline());
  }

  @Override
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (!userStatusRepository.existsByUserId(userId)) {
      throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
    }
    userStatusRepository.deleteByUserId(userId);

    // 유저가 삭제되면, 모든 채널에서 해당 유저 삭제
    channelRepository.findAll()
        .forEach(channel -> channel.removeParticipant(user));

    userRepository.deleteById(userId);
  }
}
