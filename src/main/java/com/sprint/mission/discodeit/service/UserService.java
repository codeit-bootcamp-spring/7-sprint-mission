package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;


import com.sprint.mission.discodeit.domain.exception.DuplicateException;
import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
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

        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username());


        UserEntity userEntity = mapper.toUserEntity(user);
        UserEntity savedEntity = userRepository.save(userEntity);
        UserDto userDto = mapper.toUserDto(savedEntity);

        if (file != null && !file.isEmpty()) {
            BinaryContentDto content = binaryContentService.put(savedEntity.getId(), file);
            userDto.setProfile(content);
        }

        return userDto;
    }

    @Transactional
    public UserDto updateUserInfo(UUID id, UserUpdateRequest updateDto, MultipartFile file) {

        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));

        if (updateDto.newUsername() != null) {
            userEntity.setUsername(updateDto.newUsername());
        }
        if (updateDto.newPassword() != null) {
            userEntity.setPassword(updateDto.newPassword());
        }
        if (updateDto.newEmail() != null) {
            userEntity.setPassword(updateDto.newEmail());
        }
        UserDto userDto = mapper.toUserDto(userEntity);
        if (file != null) {
            BinaryContentDto content = binaryContentService.put(userEntity.getId(), file);
            userEntity.setProfileId(content.getId());
            userDto.setProfile(content);
        }
        return userDto;
    }


    @Transactional
    public void delete(UUID id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        userRepository.delete(userEntity);
        //우선은 프로필만 삭제. 메세지에 저장된 binaryContent는 어떻게 삭제할 지 생각좀 해보겠음
        binaryContentService.deleteFile(userEntity.getId());
    }


    @Transactional
    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userEntity -> {
                    UserDto userDto = mapper.toUserDto(userEntity);
                    BinaryContentDto binaryContent = binaryContentService.getBinaryContent(userEntity.getId());
                    userDto.setProfile(binaryContent);
                    return userDto;
                })
                .toList();
    }


    public UserDto login(String loginId, String password) {

        UserEntity userEntity = userRepository
                .findByUsername(loginId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        if (!userEntity.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }

        return mapper.toUserDto(userEntity);

    }


    public UserDto markOnline(UUID id, Instant lastAt) {
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        userEntity.setLastActiveAt(Instant.now());
        return mapper.toUserDto(userEntity);
    }


}
