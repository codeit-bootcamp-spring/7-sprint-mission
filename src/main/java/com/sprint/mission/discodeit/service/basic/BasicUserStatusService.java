package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusViewRes;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    @Override
    public UserStatus create(UserStatus userStatus) {
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_STATUS_MISSING)
        );
    }

    // ===== 🎯 Controller Direct (DTO 반환) =====
    @Override
    public UserStatusViewRes findById(@NonNull UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
                ()-> new CustomException(ErrorCode.USERSTATUS_NOT_FOUND)
        );
        return UserStatusViewRes.from(userStatus);
    }

    // ===== 🔧 Controller Direct (단일 도메인 / void) =====
    @Override
    public void updateOnlineAt(@NonNull UUID id) {
        userStatusRepository.updateOnlineAt(id);
    }

    @Override
    public void updateOfflineAt(@NonNull UUID id) {
        userStatusRepository.updateOfflineAt(id);
    }

    @Override
    public void update(@NonNull UUID id) {
        userStatusRepository.update(id);
    }

    @Override
    public void updateByUserId(@NonNull UUID userId) {
        userStatusRepository.updateByUserId(userId);
    }

    @Override
    public void delete(@NonNull UUID id) {
        userStatusRepository.delete(id);
    }
}
