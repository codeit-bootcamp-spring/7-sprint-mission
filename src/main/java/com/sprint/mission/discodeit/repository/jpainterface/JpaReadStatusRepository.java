package com.sprint.mission.discodeit.repository.jpainterface;

import com.sprint.mission.discodeit.entity.ReadStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaReadStatusRepository extends JpaRepository<ReadStatusEntity, UUID> {

}
