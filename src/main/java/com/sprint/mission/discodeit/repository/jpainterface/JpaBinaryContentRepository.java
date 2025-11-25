package com.sprint.mission.discodeit.repository.jpainterface;


import com.sprint.mission.discodeit.entity.BinaryContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaBinaryContentRepository extends JpaRepository<BinaryContentEntity, UUID> {
}
