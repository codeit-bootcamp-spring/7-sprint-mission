package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.BinaryContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContentEntity, UUID> {
}
