package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public void createUserStatus(CreateUserStatusRequestDto request) {
        if (!userRepository.isExist(request.getUserId())) {
            throw new IllegalArgumentException("user status를 생성할 유저가 존재하지 않습니다.");
        } else if (userStatusRepository.isExist(request.getUserId())) {
            throw new IllegalArgumentException("해당 유저의 user status가 이미 존재합니다.");
        }

        UserStatus newUserStatus = new UserStatus(request.getUserId());
        userStatusRepository.save(newUserStatus);
    }

    @Override
    public UserStatus findUserStatus(UUID id) {
        return userStatusRepository.findById(id);
    }

    @Override
    public List<UserStatus> findAllUserStatus() {
        return new ArrayList<>(userStatusRepository.findAll());
    }

    @Override
    public void updateUserStatus(UpdateUserStatusRequestDto request) {
        UserStatus newUserStatus = userStatusRepository.findById(request.getUserId());
        newUserStatus.setUpdatedAt(); // 로그인 시간 변경
        userStatusRepository.save(newUserStatus);
    }

    @Override
    public void updateByUserId(UUID userId) {
        userStatusRepository.findAll().stream()
                .filter(us -> userId.equals(us.getId()))
                .findFirst()
                .ifPresentOrElse(us -> {
                    us.setUpdatedAt();
                    userStatusRepository.save(us);
                },
                () -> {
                    throw new IllegalArgumentException("user status를 저장할 유저가 존재하지 않습니다.");
                });
    }

    @Override
    public void deleteUserStatus(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
