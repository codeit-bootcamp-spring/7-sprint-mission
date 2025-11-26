package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.exception.DuplicateException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final BinaryContentService binaryContentService;
    private final UserMapper mapper;

    @Transactional
    public UserDto createUser(UserCreateRequest requestDto, MultipartFile file) {

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new DuplicateException("이미 등록된 이메일입니다");
        }
        if (userRepository.existsByUsername(requestDto.username())) {
            throw new DuplicateException("이미 등록된 아이디입니다");
        }

        User user = new User();

        User save = userRepository.save(user);


        if (file != null && !file.isEmpty()) {
            BinaryContent content = binaryContentService.put(save.getId(), file);
            user.setProfile(content);

        }
        return mapper.toDto(save);
    }

    @Transactional
    public UserDto updateUserInfo(UUID id, UserUpdateRequest updateDto, MultipartFile file) {

        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));

        if (updateDto.newUsername() != null) {
            user.setUsername(updateDto.newUsername());
        }
        if (updateDto.newPassword() != null) {
            user.setPassword(updateDto.newPassword());
        }
        if (updateDto.newEmail() != null) {
            user.setPassword(updateDto.newEmail());
        }

        if (file != null) {
            BinaryContent content = binaryContentService.put(user.getId(), file);
            user.setProfile(content);
        }
        return mapper.toDto(user);
    }


    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        userRepository.delete(user);
        //우선은 프로필만 삭제. 메세지에 저장된 binaryContent는 어떻게 삭제할 지 생각좀 해보겠음
        binaryContentService.deleteFile(user.getId());
    }


    @Transactional
    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    public UserDto login(String loginId, String password) {

        User user = userRepository
                .findByUsername(loginId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }

        return mapper.toDto(user);

    }


    public UserDto markOnline(UUID id, Instant lastAt) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        user.setLastActiveAt(Instant.now());
        return mapper.toDto(user);
    }


}
