package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.enums.OnlineStatus;
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
    public UserResponseDto getByUserId(String userId) {
        return UserResponseDto.toDto(userRepository.findByUserId(userId));
    }

    @Override
    public UserResponseDto getById(UUID uuid) {
        return UserResponseDto.toDto(userRepository.findById(uuid));
    }


    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::toDto)
                .toList();
    }

    @Override
    public List<UserResponseDto> getOnlineUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getOnlineStatus() != OnlineStatus.OFFLINE)
                .map(UserResponseDto::toDto)
                .toList();
    }


    @Override
    public void signIn(UserCreateRequestDto dto) {
        User user = new User(dto.id(), dto.passwd(), dto.email(), dto.displayName());
        if (dto.profileImage() != null) {
            user.setProfileImage(dto.profileImage());
        }
        userRepository.save(user);
    }

    @Override
    public UserResponseDto login(String id, String passwd) {
        User user = userRepository.findByUserId(id);
        if (!user.getPasswd().equals(passwd))
            throw new IllegalArgumentException("아이디와 비밀번호가 일치하지 않습니다.");

        user.setOnlineStatus(OnlineStatus.ONLINE);
        userRepository.update(user);
        return UserResponseDto.toDto(user);
    }

    @Override
    public void update(UserUpdateRequestDto dto) {
        User user = userRepository.findByUserId(dto.userId());
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
        if (dto.profileImage() != null) {
            user.setProfileImage(dto.profileImage());
        }

        userRepository.update(user);
    }

    @Override
    public void deleteByUserId(String id){
        User user = userRepository.findByUserId(id);
        userRepository.delete(user);
        readStatusRepository.deleteAllByUser(user);
        binaryContentRepository.delete(user.getProfileImage());
    }

    @Override
    public void deleteById(UUID uuid) {
        User user = userRepository.findById(uuid);
        userRepository.delete(user);
        readStatusRepository.deleteAllByUser(user);
        binaryContentRepository.delete(user.getProfileImage());
    }
}
