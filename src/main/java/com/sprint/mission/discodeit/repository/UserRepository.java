package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {

    // 공통 CRUD 메서드는 Repository에서 상속받음
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
