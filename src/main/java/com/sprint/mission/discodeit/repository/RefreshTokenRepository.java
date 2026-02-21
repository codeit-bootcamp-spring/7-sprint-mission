//package com.sprint.mission.discodeit.repository;
//
//import com.sprint.mission.discodeit.entity.RefreshToken;
//import java.util.Optional;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//
//    Optional<RefreshToken> findByToken(String token);
//
//    Optional<RefreshToken> findByUserId(Long userId);
//
//    void deleteByUserId(Long userId);
//
//    void deleteByToken(String token);
//
//}
