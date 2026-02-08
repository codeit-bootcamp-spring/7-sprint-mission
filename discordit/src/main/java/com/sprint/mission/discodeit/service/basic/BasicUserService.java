package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.enums.Roles;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.dto.entity.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.mapper.UserMapper;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserStatusService userStatusService;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto get(UUID id) {
        log.info("사용자 조회 요청 들어옴 - {}", id);
        return UserMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }


    @Override
    public List<UserDto> getAllUsers() {
        log.info("모든 사용자 조회 요청 들어옴");
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto signIn(UserCreateRequest dto, MultipartFile profile) {
        log.info("사용자 생성 요청 들어옴 - username : {}", dto.username());
        User user = new User(dto.username(), passwordEncoder.encode(dto.password()), dto.email());
        log.debug("사용자 생성 완료 - id : {}", user.getId());
        if (profile != null) {
            BinaryContent saved = new BinaryContent(profile.getOriginalFilename(), profile.getSize(), profile.getContentType());
            binaryContentRepository.save(saved);
            try {
                binaryContentStorage.put(saved.getId(), profile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.setProfile(saved);
            log.debug("사용자 프로필 이미지 저장 완료.");
        }
        userRepository.save(user);
        userStatusRepository.save(new UserStatus(user));
        log.debug("UserStatus 생성 완료.");
        log.info("사용자 생성 완료 - id : {}", user.getId());
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto update(UUID id, UserUpdateRequest dto, MultipartFile profile) {
        log.info("사용자 정보 수정 요청 들어옴 - id : {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (dto.newPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            log.debug("{}의 비밀번호 수정 완료",user.getUsername());
        }
        if (dto.newUsername() != null) {
            user.setUsername(dto.newUsername());
            log.debug("{}로 아이디 수정 완료",user.getUsername());
        }
        if (dto.newEmail() != null) {
            user.setEmail(dto.newEmail());
            log.debug("{}의 이메일 수정 완료",user.getUsername());
        }

        if (profile != null) {
            BinaryContent saved = new BinaryContent(profile.getOriginalFilename(), profile.getSize(), profile.getContentType());
            try {
                binaryContentStorage.put(saved.getId(), profile.getBytes());
            } catch (IOException e) {
                log.warn("파일 저장 중 예상치 못한 오류 발생");
                throw new RuntimeException(e);
            }
        }
        userRepository.save(user);
        log.info("회원 정보 수정 완료 - id : {}",user.getId());
        return UserMapper.toDto(user);
    }

    @Override
    public void delete(UUID id) {
        log.info("사용자 삭제 요청 들어옴 - id : {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
        log.debug("사용자 삭제 완료 - id : {}", id);
        readStatusRepository.deleteAllByUser(user);
        log.debug("관련된 UserStatus 삭제 완료");
        if (user.getProfile() != null) {
            binaryContentRepository.delete(user.getProfile());
            log.debug("삭제된 사용자의 프로필 이미지 삭제 완료");
        }
        log.info("사용자 삭제 완료");
    }

    @Override
    public UserDto updateRole(UUID id, Roles role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.updateRole(role);
        return UserMapper.toDto(user);
    }
}
