package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(CreateUserDto createUserDto, Optional<CreateBinaryContentDto> createBinaryContentDto) {
        if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
            throw UserAlreadyExistsException.byUsername(createUserDto.username());
        }
        if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw UserAlreadyExistsException.byEmail(createUserDto.email());
        }

        BinaryContent profile = createBinaryContentDto
                .map(this::saveBinaryContent)
                .orElse(null);

        User user = new User(createUserDto.username(), createUserDto.email(), createUserDto.password(), profile);

        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

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
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        BinaryContent profile = createBinaryContentDto
                .map(this::saveBinaryContent)
                .orElse(null);

        user.updateUser(updateUserDto.newUsername(),
                updateUserDto.newPassword(),
                updateUserDto.newEmail(),
                profile
        );

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        userRepository.delete(user);
    }

    private BinaryContent saveBinaryContent(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = BinaryContent.builder()
                .fileName(createBinaryContentDto.fileName())
                .contentType(createBinaryContentDto.contentType())
                .size(createBinaryContentDto.size())
                .build();

        binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(binaryContent.getId(), createBinaryContentDto.bytes());

        return binaryContent;
    }
}
