package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContentEntity, String> {
//    void save(BinaryContent binaryContent);
//
//    void remove(BinaryContent binaryContent);
//
//    Optional<BinaryContent> findById(UUID id);
//
//    List<BinaryContent> findAll();
}
