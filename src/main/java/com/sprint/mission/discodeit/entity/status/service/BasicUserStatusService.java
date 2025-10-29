package com.sprint.mission.discodeit.entity.status.service;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.entity.status.dto.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.status.dto.UserStatusInfoDto;
import com.sprint.mission.discodeit.entity.status.dto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.status.repository.UserStatusRepository;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusInfoDto createUserStatus(UserStatusCreateDto createDto) {
        userStatusRepository.findStatusByUserId(createDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        userStatusRepository.findStatusByUserId(createDto.userId()).ifPresent(userStatus -> {
            throw new DuplicateRequestException("이미 존재함");
        });

        UserStatus userStatus = new UserStatus(createDto.userId());
        userStatusRepository.save(userStatus);
        return UserStatusInfoDto.from(userStatus);
    }

    @Override
    public Optional<UserStatusInfoDto> findStatusById(UUID id) {
        return userStatusRepository.findById(id).map(UserStatusInfoDto::from);
    }

    @Override
    public List<UserStatusInfoDto> findAllStatus() {
        return userStatusRepository.findAll().stream().map(UserStatusInfoDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserStatusInfoDto> updateStatus(UserStatusUpdateDto updateDto) {
        return userStatusRepository.findStatusByUserId(updateDto.userStatusId())
                .map(userStatus -> {
                    userStatus.updateLastAccess();
                    userStatusRepository.save(userStatus);
                    return UserStatusInfoDto.from(userStatus);
                });
    }

    @Override
    public void deleteUserStatusById(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
