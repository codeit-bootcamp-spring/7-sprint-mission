package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.enums.Roles;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.dto.entity.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.mapper.UserMapper;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.dto.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.dto.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher publisher;

    @Override
    public UserDto get(UUID id) {
        log.info("사용자 조회 요청 들어옴 - {}", id);
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }


    @Override
    public List<UserDto> getAllUsers() {
        log.info("모든 사용자 조회 요청 들어옴");
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto signIn(UserCreateRequest dto, MultipartFile file) {
        log.info("사용자 생성 요청 들어옴 - username : {}", dto.username());
        User user = new User(dto.username(), passwordEncoder.encode(dto.password()), dto.email());
        log.debug("사용자 생성 완료 - id : {}", user.getId());
        if (file != null) {
            BinaryContent profile = new BinaryContent(file.getOriginalFilename(), file.getSize(), file.getContentType());
            try {
                publisher.publishEvent(new BinaryContentCreatedEvent(profile, file.getBytes()));
            } catch (IOException e) {
                log.error("파일 저장 중 예상치 못한 오류 발생");
                profile.uploadFailed();
            }
            user.setProfile(profile);
        }
        userRepository.save(user);
        log.info("사용자 생성 완료 - id : {}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UUID id, UserUpdateRequest dto, MultipartFile file) {
        log.info("사용자 정보 수정 요청 들어옴 - id : {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (dto.newPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            log.debug("{}의 비밀번호 수정 완료", user.getUsername());
        }
        if (dto.newUsername() != null) {
            user.setUsername(dto.newUsername());
            log.debug("{}로 아이디 수정 완료", user.getUsername());
        }
        if (dto.newEmail() != null) {
            user.setEmail(dto.newEmail());
            log.debug("{}의 이메일 수정 완료", user.getUsername());
        }

        if (file != null) {
            BinaryContent profile = new BinaryContent(file.getOriginalFilename(), file.getSize(), file.getContentType());
            try {
                publisher.publishEvent(new BinaryContentCreatedEvent(profile, file.getBytes()));
            } catch (IOException e) {
                log.error("파일 저장 중 예상치 못한 오류 발생");
                profile.uploadFailed();
            }
            user.setProfile(profile);
        }
        userRepository.save(user);
        log.info("회원 정보 수정 완료 - id : {}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    public void delete(UUID id) {
        log.info("사용자 삭제 요청 들어옴 - id : {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (user.getProfile() != null) {
            binaryContentRepository.delete(user.getProfile());
            log.debug("삭제된 사용자의 프로필 이미지 삭제 완료");
        }
        userRepository.delete(user);
        log.debug("사용자 삭제 완료 - id : {}", id);
    }

    @Override
    @Transactional
    public UserDto updateRole(UUID id, Roles role) {
        log.info("권한 변경 요청 들어옴. : {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        publisher.publishEvent(new RoleUpdatedEvent(Instant.now(), id, user.getRole(), role));
        user.updateRole(role);
        return userMapper.toDto(user);
    }


}
