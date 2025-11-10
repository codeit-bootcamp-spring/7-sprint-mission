package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository {

    void save(UserStatus userStatus);

    Optional<UserStatus> findById(UUID id);

    List<UserStatus> findAll();

    // 유저가 로그인한 경우 시간 갱신
    void update(UserStatus status);

    void deleteById(UUID id);
}
