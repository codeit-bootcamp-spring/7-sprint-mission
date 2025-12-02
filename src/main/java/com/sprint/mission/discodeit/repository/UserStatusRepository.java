package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
    UserStatus findByUser(User user);
//    public UserStatus createUserStatus(UserStatus userStatus);
//    public void deleteUserStatus(UUID userStatusId);
//
//    public void updateUserStatus(UserStatus userStatus);
//
//    public Optional<UserStatus> readUserStatus(UUID userStatusId);
//    public List<UserStatus> readAllUserStatus();
//    public boolean isUserStatusExist(UUID userStatusId);
//    public void resetRepository();
}
