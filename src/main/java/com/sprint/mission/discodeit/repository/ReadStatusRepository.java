package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, String> {

//    void save(ReadStatus readStatus);
//
//    void remove(UUID id);
//
//    Optional<ReadStatus> findById(UUID id);
//
//    List<ReadStatus> findAll();
}
