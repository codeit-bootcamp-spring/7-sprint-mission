package com.sprint.mission.discodeit.repository.jpainterface;

import com.sprint.mission.discodeit.entity.ReadStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReadStatusRepository extends JpaRepository<ReadStatusEntity, String> {

}
