package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Override
    @Transactional
    public UserStatusDto createUserStatus(UserStatusCreateRequest requestDto) {

        User user = userRepository.findById(requestDto.userId())
                        .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        userStatusRepository.findByUserId(requestDto.userId()).ifPresent(userStatus -> {
            throw new DuplicateEmailException("이미 존재함"); // 임시
        });

        UserStatus userStatus = new UserStatus(user);
        userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatusDto findStatusById(UUID id) {
        UserStatus status = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));

        return userStatusMapper.toDto(status);
    }

    @Override
    @Transactional
    public UserStatusDto updateStatusByUserId(UUID userId, UserStatusUpdateRequest requestDto) {
        UserStatus status = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        status.updateLastActiveAt(requestDto.newLastActiveAt());
        userStatusRepository.save(status);
        return userStatusMapper.toDto(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatusDto> findAllStatus() {
        return userStatusRepository.findAll().stream().map(userStatusMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserStatusDto updateStatusById(UUID id, UserStatusUpdateRequest updateDto) {
        UserStatus status = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));

        status.updateLastActiveAt(updateDto.newLastActiveAt());
        userStatusRepository.save(status);
        return userStatusMapper.toDto(status);

    }

    @Override
    @Transactional
    public void deleteUserStatusById(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
