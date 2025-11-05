package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    @Override
    public UserStatus createUserStatus(CreateUserStatusDto createUserStatusDto) {
        if(!userRepository.existsById(createUserStatusDto.userId())) {
            throw new NoSuchElementException("존재하지 않는 유저입니다." + createUserStatusDto.userId());
        }

        if(userStatusRepository.findByUserId(createUserStatusDto.userId()).isPresent()) {
            throw new NoSuchElementException("이미 존재하는 UserStatus입니다." + createUserStatusDto.userId());
        }

        UserStatus userstatus = new UserStatus(createUserStatusDto.userId(), createUserStatusDto.loginAt());
        userStatusRepository.save(userstatus);
        return userstatus;
    }

    @Override
    public UserStatus getUserStatus(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus입니다."));
    }

    @Override
    public List<UserStatus> getAllUserStatuses() {
        return userStatusRepository.findAll();
    }

    @Override
    public void updateUserStatus(UUID userStatusId, UpdateUserStatusDto updateUserStatusDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus입니다." + userStatusId));

        userStatus.update(updateUserStatusDto.loginAt());
        userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateStatusByUserId(UUID userId, UpdateUserStatusDto updateUserStatusDto) {
        Instant lastActiveAt = updateUserStatusDto.loginAt();
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));
        userStatus.update(lastActiveAt);
        userStatusRepository.save(userStatus);

        return userStatus;
    }

    @Override
    public void deleteById(UUID userStatusId) {
        if(!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException("찾을 수 없는 UserStauts입니다." + userStatusId);
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
