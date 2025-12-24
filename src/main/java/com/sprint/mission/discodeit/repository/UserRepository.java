package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("select u from User u" +
            " left join fetch u.profile" +
            " where u.id = :userId")
    Optional<User> findByIdWithBinaryContent(
            @Param("userId") UUID userId);

    boolean existsByEmailOrUsername(String email, String username);
    Optional<User> findByUsername(String username);
}
