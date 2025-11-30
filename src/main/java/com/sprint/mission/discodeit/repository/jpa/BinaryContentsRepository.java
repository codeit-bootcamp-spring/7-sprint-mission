package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentsRepository extends JpaRepository<BinaryContent, UUID> {
//    void save(BinaryContent content); // save/ saveAndFlush
//    void deleteById(UUID id);
//    Optional<BinaryContent> findById(UUID id); //findById/  getById // findAllById
//    List<BinaryContent> findAll();
}
