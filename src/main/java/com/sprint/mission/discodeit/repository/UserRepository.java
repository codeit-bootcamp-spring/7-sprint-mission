package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    /***
     * N+1문제 개선, 유저 전체 조회
     */
    @Query(value = "SELECT u FROM User u "
            + "LEFT JOIN FETCH u.profile")
    List<User> findAllWithProfile();
}
