package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatusEntity, UUID> {

}
