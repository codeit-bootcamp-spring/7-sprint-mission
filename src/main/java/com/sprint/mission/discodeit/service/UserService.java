package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.exception.DuplicateException;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final BinaryContentService binaryContentService;

    @Transactional
    public UserDto createUser(UserCreateRequest requestDto, MultipartFile file) {

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new DuplicateException("이미 등록된 이메일입니다");
        }
        if (userRepository.existsByUsername(requestDto.username())) {
            throw new DuplicateException("이미 등록된 아이디입니다");
        }

        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username());

        User save = userRepository.save(user);

        BinaryContentDto binaryContentDto = null;
        if (file != null && !file.isEmpty()) {
            BinaryContent content = binaryContentService.put(save.getId(), file);
            userRepository.updateProfileId(save.getId(), content.getId());
            save.setProfile(content.getId());
            binaryContentDto = BinaryContentDto.from(content);
        }

        return UserDto.from(save, binaryContentDto);
    }

    public UserDto updateUserInfo(String id, UserUpdateRequest updateDto, MultipartFile file) {

        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));

        if (updateDto.newUsername() != null) {
            user.updateUsername(updateDto.newUsername());
        }
        if (updateDto.newPassword() != null) {
            user.updatePassword(updateDto.newPassword());
        }
        if (updateDto.newEmail() != null) {
            user.updateEmail(updateDto.newEmail());
        }

        if (file != null) {
            BinaryContent content = binaryContentService.put(user.getId(), file);
            user.setProfile(content.getId());
        }
        BinaryContentDto binaryContent = binaryContentService.getBinaryContent(user.getProfileId());
        User save = userRepository.save(user);
        return UserDto.from(save, binaryContent);
    }


    public void delete(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        userRepository.delete(user);
        binaryContentService.deleteUserFolder(user.getId());
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user ->
                        UserDto.from(user, binaryContentService.getBinaryContent(user.getProfileId())))
                .toList();
    }


    public UserDto login(String loginId, String password) {
        User user =
                userRepository
                        .findByUsername(loginId)
                        .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }
        return UserDto.from(user, null);

    }


    public UserStatusDto markOnline(String id, Instant lastAt) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        user.updateLastActiveAt(lastAt);
        userRepository.save(user);
        return UserStatusDto.from(user);
    }


}
