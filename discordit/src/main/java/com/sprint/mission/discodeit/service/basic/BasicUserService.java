package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.*;
import com.sprint.mission.discodeit.dto.user.response.UserResponseV2;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
import org.springframework.web.multipart.MultipartFile;

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
    public UserResponseV2 get(UUID id) {
        return UserResponseV2.toDto(userRepository.find(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }


    @Override
    public List<UserResponseV2> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseV2::toDto)
                .toList();
    }

    @Override
    public List<UserResponseV2> getOnlineUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getOnlineStatus() != OnlineStatus.OFFLINE)
                .map(UserResponseV2::toDto)
                .toList();
    }


    @Override
    public UserResponseV2 signIn(UserCreateRequest dto) {
        User user = new User(dto.id(), dto.passwd(), dto.email(), dto.displayName());
        if (dto.profileImageId() != null) {
            user.setProfileImage(binaryContentRepository.findById(dto.profileImageId())
                    .orElseThrow(() -> new BinaryContentNotFoundException(dto.profileImageId())));
        }
        userRepository.save(user);
        return UserResponseV2.toDto(user);
    }

    @Override
    public UserResponseV2 update(UUID id, UserUpdateRequest dto, MultipartFile profile) {
        User user = userRepository.find(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (dto.newPassword() != null) {
            user.setPasswd(dto.newPassword());
        }
        if (dto.newUsername() != null) {
            user.setDisplayName(dto.newUsername());
        }
        if (dto.newEmail() != null) {
            user.setEmail(dto.newEmail());
        }

        // 프로필 저장은 다음 미션에..
//        if (profile != null) {
//            if (user.getProfileImage() != null) {
//                binaryContentRepository.delete(user.getProfileImage());
//            }
//            BinaryContent content = new BinaryContent(profile);
//            binaryContentRepository.save(content);
//            user.setProfileImage(content);
//        }
        userRepository.update(user);
        return UserResponseV2.toDto(user);
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.find(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
        readStatusRepository.deleteAllByUser(user);
        if (user.getProfileImage() != null) {
            binaryContentRepository.delete(user.getProfileImage());
        }
    }
}
