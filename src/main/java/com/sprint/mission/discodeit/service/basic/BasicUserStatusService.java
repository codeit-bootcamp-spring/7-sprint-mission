package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.update.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    // 중복 메서드 만들기
    private UserStatus getUserStatus(UUID userStatusId) {
        return userStatusRepository.findByUserId(userStatusId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
    }

    @Override
    public UserStatus createUserStatus(CreateUserStatusRequestDto requestDto) {
        UUID userId = requestDto.userId();
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("유저가 없습니다.");
        }
        if (userStatusRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("이미 존재합니다.");
        }

        UserStatus newStatus = new UserStatus(requestDto.userId());
        return userStatusRepository.save(newStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return getUserStatus(userStatusId);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus updateUserStatus(UUID userStatusId, UpdateUserStatusDto updateDto) {
        UserStatus userStatus = getUserStatus(userStatusId);
        userStatus.statusUpdate(updateDto.newAccessTime());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UpdateUserStatusDto updateDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 상태가 없습니다."));
        userStatus.statusUpdate(updateDto.newAccessTime());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        getUserStatus(userStatusId);
        userStatusRepository.delete(userStatusId);
    }
}
