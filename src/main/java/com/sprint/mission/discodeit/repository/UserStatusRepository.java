package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserStatusRepository {

    void save(UserStatus userStatus);

    UserStatus findById(UUID id);

    // 유저가 로그인한 경우 시간 갱신
    void updateLoginTime(UUID id);

    void deleteById(UUID id);
}
