package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.*;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import com.sprint.mission.discodeit.exceptions.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public UserResponse getByUserId(String userId) {
        return UserResponse.toDto(findUserAndHandleConflict(userId));
    }

    @Override
    public UserResponse get(UUID uuid) {
        return UserResponse.toDto(findUserAndHandleConflict(uuid));
    }


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::toDto)
                .toList();
    }

    @Override
    public List<UserResponse> getOnlineUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getOnlineStatus() != OnlineStatus.OFFLINE)
                .map(UserResponse::toDto)
                .toList();
    }


    @Override
    public UserResponse signIn(UserCreateRequest dto) {
        User user = new User(dto.id(), dto.passwd(), dto.email(), dto.displayName());
        if (dto.profileImageId() != null) {
            user.setProfileImage(binaryContentRepository.findById(dto.profileImageId())
                    .orElseThrow(() -> new BinaryContentNotFoundException(dto.profileImageId())));
        }
        userRepository.save(user);
        return UserResponse.toDto(user);
    }

    // TODO: Auth만들기
    @Override
    public UserResponse login(String id, String passwd) {
        User user = findUserAndHandleConflict(id);
        if (!user.getPasswd().equals(passwd))
            throw new IllegalArgumentException("아이디와 비밀번호가 일치하지 않습니다.");

        user.setOnlineStatus(OnlineStatus.ONLINE);
        userRepository.update(user);
        return UserResponse.toDto(user);
    }

    @Override
    public UserResponse update(UserUpdateRequest dto) {
        User user = findUserAndHandleConflict(dto.userId());
        if (dto.passwd() != null) {
            user.setPasswd(dto.passwd());
        }
        if (dto.displayName() != null) {
            user.setDisplayName(dto.displayName());
        }
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.bio() != null) {
            user.setBio(dto.bio());
        }
        if (dto.onlineStatus() != null) {
            user.setOnlineStatus(dto.onlineStatus());
        }
        if (dto.profileImageId() != null) {
            user.setProfileImage(binaryContentRepository.findById(dto.profileImageId())
                    .orElseThrow(() -> new BinaryContentNotFoundException(dto.profileImageId())));
        }

        userRepository.update(user);

        return UserResponse.toDto(user);
    }

    @Override
    public void deleteByUserId(UserDeleteRequest dto){
        User user = findUserAndHandleConflict(dto.userId());
        userRepository.delete(user);
        readStatusRepository.deleteAllByUser(user);
        binaryContentRepository.delete(user.getProfileImage());
    }

    @Override
    public void deleteById(UUID uuid) {
        User user = findUserAndHandleConflict(uuid);
        userRepository.delete(user);
        readStatusRepository.deleteAllByUser(user);
        binaryContentRepository.delete(user.getProfileImage());
    }

    private User findUserAndHandleConflict(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException(uuid));
    }

    private User findUserAndHandleConflict(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
