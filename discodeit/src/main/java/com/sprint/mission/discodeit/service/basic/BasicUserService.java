package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 유저 이름입니다.: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.: " + request.getEmail());
        }

        User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
        User savedUser = userRepository.save(user);

        // 유저가 생성된 다음 생성되는 부가 정보
        UserStatus userStatus = new UserStatus(savedUser.getId());
        userStatusRepository.save(userStatus);

        return UserResponse.from(savedUser, userStatus.isOnline());
    }

    @Override
    public UserResponse find(UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저 " + userId + "를 찾을 수 없다."));

        boolean isOnline = userStatusRepository.findByUserId(userId)
                .map(UserStatus::isOnline)
                .orElse(false);

        return UserResponse.from(user, isOnline);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(u -> {
                    boolean online = userStatusRepository.findByUserId(u.getId())
                            .map(UserStatus::isOnline)
                            .orElse(false);
                    return UserResponse.from(u, online);
                })
                .toList();
    }

    @Override
    public UserResponse update(UpdateUserRequest request) {

        User user = userRepository.findById(request.getUserId()).orElseThrow(()-> new NoSuchElementException("유저를 찾을 수 없습니다."));

        if (request.getUsername() != null
            && !request.getUsername().equals(user.getUsername())
            && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 유저 이름입니다.");
        }
        if (request.getEmail() != null
        && request.getEmail().equals(user.getEmail())
        && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User updated = userRepository.save(user);
        boolean online = userStatusRepository.findByUserId(updated.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        return UserResponse.from(updated, online);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        if (userStatusRepository.existsByUserId(userId)) {
            userStatusRepository.deleteByUserId(userId);
        }
        userRepository.deleteById(userId);

    }
}
