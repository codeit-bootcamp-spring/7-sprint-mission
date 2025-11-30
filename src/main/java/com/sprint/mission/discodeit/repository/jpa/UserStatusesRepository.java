package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusesRepository extends JpaRepository<UserStatus, UUID> {
//    void save(T model);
//    void deleteById(UUID id);
//    Optional<T> findById(UUID id);
//    List<T> findAll();

    Optional<UserStatus> findUserStatusByUserId(UUID userId);
}
