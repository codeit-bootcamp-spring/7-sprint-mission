package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusRequestDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponseDto createUserStatus(UserStatusRequestDto requestDto) {

        userRepository.findById(requestDto.userId())
                        .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        userStatusRepository.findStatusByUserId(requestDto.userId()).ifPresent(userStatus -> {
            throw new DuplicateRequestException("이미 존재함");
        });

        UserStatus userStatus = new UserStatus(requestDto.userId());
        userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto findStatusById(UUID id) {
        UserStatus status = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));

        return UserStatusResponseDto.from(status);
    }

    @Override
    public List<UserStatusResponseDto> findAllStatus() {
        return userStatusRepository.findAll().stream().map(UserStatusResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusResponseDto updateStatus(UserStatusUpdateDto updateDto) {
        UserStatus status = userStatusRepository.findById(updateDto.userStatusId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));
        status.updateLastAccess();
        userStatusRepository.save(status);
        return UserStatusResponseDto.from(status);

    }

    @Override
    public void deleteUserStatusById(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
