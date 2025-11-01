package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserIdStatusDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    // 중복 메서드 만들기
    private UserStatus getUserStatus(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new IllegalArgumentException("유저 상태를 찾을 수 없습니다."));
    }

    @Override
    public UserStatusResponseDto createUserStatus(CreateUserStatusRequestDto requestDto) {
        UUID userId = requestDto.userId();
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("유저가 없습니다.");
        }
        if (userStatusRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("이미 존재합니다.");
        }

        UserStatus newStatus = new UserStatus(requestDto.userId());
        UserStatus userStatus = userStatusRepository.save(newStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto find(UUID userStatusId) {

        UserStatus userStatus = getUserStatus(userStatusId);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        List<UserStatusResponseDto> dtoList = new ArrayList<>();
        for(UserStatus userStatus : userStatuses){
            dtoList.add(UserStatusResponseDto.from(userStatus));
        }
        return dtoList;
    }

    @Override
    public UserStatusResponseDto updateUserStatus(UpdateUserStatusDto updateDto) {
        UserStatus userStatus = getUserStatus(updateDto.userStatusId());
        userStatus.statusUpdate(Instant.now());
        UserStatus userstatus = userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userstatus);
    }

    @Override
    public UserStatusResponseDto updateByUserId(UpdateUserIdStatusDto updateDto) {
        UserStatus user = userStatusRepository.findByUserId(updateDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        user.statusUpdate(Instant.now());
        UserStatus userstatus = userStatusRepository.save(user);
        return UserStatusResponseDto.from(userstatus);
    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        getUserStatus(userStatusId);
        userStatusRepository.delete(userStatusId);
    }
}
