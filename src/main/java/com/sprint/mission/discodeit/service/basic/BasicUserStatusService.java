package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus createUserStatus(UserStatusCreateRequest requestDto) {

        userRepository.findById(requestDto.userId())
                        .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        userStatusRepository.findStatusByUserId(requestDto.userId()).ifPresent(userStatus -> {
            throw new DuplicateRequestException("이미 존재함");
        });

        UserStatus userStatus = new UserStatus(requestDto.userId());
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatusDto findStatusById(UUID id) {
        UserStatus status = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));

        return UserStatusDto.from(status);
    }

    @Override
    public UserStatus updateStatusByUserId(UUID userId, UserStatusUpdateRequest requestDto) {
        UserStatus status = userStatusRepository.findStatusByUserId(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        status.updateLastAccess(requestDto.newLastActiveAt());
        userStatusRepository.save(status);
        return status;
    }

    @Override
    public List<UserStatusDto> findAllStatus() {
        return userStatusRepository.findAll().stream().map(UserStatusDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatus updateStatusById(UUID id, UserStatusUpdateRequest updateDto) {
        UserStatus status = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));

        status.updateLastAccess(updateDto.newLastActiveAt());
        userStatusRepository.save(status);
        return status;

    }

    @Override
    public void deleteUserStatusById(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
