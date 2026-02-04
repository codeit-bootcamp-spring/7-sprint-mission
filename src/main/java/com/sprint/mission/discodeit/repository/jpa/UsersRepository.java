package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, UUID> {
//    void save(T model);
//    void deleteById(UUID id);
//    Optional<T> findById(UUID id);
//    List<T> findAll();

//    Res_UserLogin isLogin(LoginRequest authServiceDto);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);

    boolean existsByRole(Role role);
}
