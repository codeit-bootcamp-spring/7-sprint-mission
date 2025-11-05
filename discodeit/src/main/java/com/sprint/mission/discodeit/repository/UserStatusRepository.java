package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    List<UserStatus> findAll();
    Optional<UserStatus> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    void deleteByUserId(UUID userid);
    // void deleteById(UUID id); // 회원 삭제


}
