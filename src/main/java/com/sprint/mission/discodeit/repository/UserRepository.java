package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // 이메일(아이디) 중복 확인용
    Optional<User> findByEmail(String email);

    // 닉네임 중복 확인용
    Optional<User> findByUsername(String userName);
}
