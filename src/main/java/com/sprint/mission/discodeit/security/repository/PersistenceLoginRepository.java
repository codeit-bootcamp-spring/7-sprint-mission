package com.sprint.mission.discodeit.security.repository;

import com.sprint.mission.discodeit.entity.PersistenceLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersistenceLoginRepository extends JpaRepository<PersistenceLogin, String> {

    List<PersistenceLogin> findByUserId(UUID userId);
    
    @Modifying
    @Query("delete from PersistenceLogin p where p.userId =:userId")
    void deleteByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("delete from PersistenceLogin p where p.lastUsed< :cutoff")
    void deleteByLastUsedBefore(@Param("cutoff") Instant cutoff);
    
    @Query("select count(p) from PersistenceLogin p where p.userId = :userId")
    long countByUserId(@Param("userId") UUID userId);

    Optional<PersistenceLogin> findBySeries(String series);
    
}
