package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.user.UserDto;
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
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryRepo;
    private final BinaryContentStorage storage;

    @Override
    public UserDto create(UserCreateRequest req,
                          Optional<BinaryContentCreateRequest> profileReq) {

        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .name(req.username())
                .email(req.email())
                .password(req.password())
                .build();

        userRepository.save(user);

        profileReq.ifPresent(p -> saveProfile(user, p));

        userStatusRepository.save(UserStatus.builder().user(user).build());

        return UserDto.from(user);
    }

    @Override
    public UserDto update(UUID id,
                          UserUpdateRequest req,
                          Optional<BinaryContentCreateRequest> profileReq) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.update(req.username());

        profileReq.ifPresent(p -> saveProfile(user, p));

        return UserDto.from(user);
    }

    @Override
    public UserDto find(UUID userid) {
        return userRepository.findById(userid)
                .map(UserDto::from)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .toList();
    }

    @Override
    public void delete(UUID userid) {
        userRepository.deleteById(userid);
    }


    // ---------------------------------
    // Private: 프로필 저장
    // ---------------------------------
    private void saveProfile(User user, BinaryContentCreateRequest req) {

        BinaryContent binary = BinaryContent.builder()
                .fileName(req.fileName())
                .contentType(req.contentType())
                .size(req.bytes().length)
                .user(user)
                .build();

        binaryRepo.save(binary);

        storage.put(binary.getId(), req.bytes());

        user.updateProfile(binary);
    }
}

