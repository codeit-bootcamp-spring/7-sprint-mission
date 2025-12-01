package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface UserRepository extends JpaRepository<User, UUID> {
    User save(User user);

    Optional<User> findById(UUID id);


    List<User> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);


    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    @Query("""
            select u
            from User u
            where u.username = :username
              and u.password = :password
            """)
    Optional<User> findByUsernameAndPassword(@Param("username") String username,
                                             @Param("password") String password);
}
