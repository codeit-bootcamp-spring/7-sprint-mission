package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // 이메일 중복 확인용
    boolean existsByEmail(String email);

    // 닉네임 중복 확인용
    boolean existsByUsername(String userName);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile")
    List<User> findAllWithProfile();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
