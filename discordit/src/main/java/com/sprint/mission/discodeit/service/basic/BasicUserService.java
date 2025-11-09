package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.*;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
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
    public UserResponse get(UUID id) {
        return UserResponse.toDto(userRepository.find(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
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
    public UserResponse login(String userId, String passwd) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (!user.getPasswd().equals(passwd))
            throw new IllegalArgumentException("아이디와 비밀번호가 일치하지 않습니다.");

        user.setOnlineStatus(OnlineStatus.ONLINE);
        userRepository.update(user);
        return UserResponse.toDto(user);
    }

    @Override
    public UserResponse update(UserUpdateRequest dto) {
        User user = userRepository.find(dto.id())
                .orElseThrow(() -> new UserNotFoundException(dto.id()));
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
    public void delete(UserDeleteRequest dto) {
        User user = userRepository.find(dto.id())
                .orElseThrow(() -> new UserNotFoundException(dto.id()));
        userRepository.delete(user);
        readStatusRepository.deleteAllByUser(user);
        if (user.getProfileImage() != null) {
            binaryContentRepository.delete(user.getProfileImage());
        }
    }
}
