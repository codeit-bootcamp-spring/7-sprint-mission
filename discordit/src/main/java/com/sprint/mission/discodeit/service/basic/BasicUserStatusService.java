package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.userStatus.UserStatusNotExistException;
import com.sprint.mission.discodeit.dto.entity.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.entity.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.userStatus.response.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusUpdateResponse create(UserStatusCreateRequest dto) {
        log.info("사용자 상태 생성 요청 들어옴 - {}", dto.userId());
        User byUserId = userRepository.findByUsername(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));
        UserStatus userStatus = new UserStatus(byUserId);
        userStatusRepository.save(userStatus);
        log.info("사용자 상태 생성 완료.");
        return UserStatusUpdateResponse.toDto(userStatus);
    }

    @Override
    public UserStatusUpdateResponse get(UUID id) {
        log.info("사용자 상태 조회 요청 들어옴 - {}", id);
        return UserStatusUpdateResponse.toDto(userStatusRepository.findById(id)
                .orElseThrow(() -> new UserStatusNotExistException(id)));
    }

    @Override
    public UserStatusUpdateResponse updateByUser(UUID userId, UserStatusUpdateRequest request) {
        log.info("사용자 상태 수정 요청 들어옴 - {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return UserStatusUpdateResponse.toDto(
                userStatusRepository.getUserStatusByUser(user)
                        .orElseThrow(() -> new UserStatusNotExistException(user)));
    }
}
