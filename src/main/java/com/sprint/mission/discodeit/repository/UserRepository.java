package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository
 * -----------------
 * 유저 데이터의 저장/조회/삭제를 담당하는 저장소 계층 인터페이스입니다.
 * (실제 데이터 저장소는 메모리, DB 등 다양하게 구현될 수 있습니다.)
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    // fetch join으로 모든 유저를 조회할 때 프로필 이미지와 상태 정보를 한번에 조회
    @Query("select u from User u left join fetch u.profile join fetch u.status")
    List<User> findAllWithProfileAndStatus();

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);
}
