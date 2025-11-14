package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterfaceUserStatusRepository extends BaseInterfaceRepository<UserStatus>{
//    FileUserStatusRepository(FileUtil fileUtil);
    void save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    Optional<UserStatus> findByUserId(UUID userID);
    List<UserStatus> findAll();
    boolean deleteById(UUID id);
}
