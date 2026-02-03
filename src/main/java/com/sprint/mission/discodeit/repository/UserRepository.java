package com.sprint.mission.discodeit.repository;


import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("select user from User user where user.id in :userIds")
    List<User> findOneUser(@Param("userIds") List<UUID> userIds);

    Optional<User> findById(UUID id);

    Optional<User> findByUserName(String userName);
    Optional<User> findByRole(Role role);
}
