package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.PersistentLogin;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {

    // findById가 기본 제공되지만 메서드 호출 및 코드 가독성을 위해 직접 선언하는 경우도 있다.
    Optional<PersistentLogin> findBySeries(String series);

    // 사용자의 모든 토큰 조회(username)
    List<PersistentLogin> findByUsername(String username);

    // 로그아웃, 비밀번호 변경 등을 진행할 때 기존의 모든 사용자 토큰을 삭제
    @Modifying
    @Query("DELETE FROM PersistentLogin p WHERE p.username = :username")
    void deleteByUsername(@Param("username") String username);

    // 오래된 토큰 정리
    @Modifying
    @Query("DELETE FROM PersistentLogin p WHERE p.lastUsed < :cutoff")
    void deleteByLastUsedBefore(@Param("cutoff") LocalDateTime cutoff);

    // 특정 사용자의 활성 토큰 수 조회
    @Query("SELECT COUNT(p) FROM PersistentLogin p WHERE p.username = :username")
    long countByUsername(@Param("username") String username);

}