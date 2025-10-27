package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.util.List;

public interface UserService {
    User getById(String id);
    List<String> getAllUsers();
    List<String> getOnlineUsers();


    void create(UserCreateRequestDto dto);
    boolean login(String id, String passwd);

    void deleteById(String id);

    void setPasswd(String id, String passwd);
    void setBio(String id, String bio);
    void setOnlineStatus(String id, OnlineStatus userStatus);
    void setProfileImage(String id, BinaryContent image);

    OnlineStatus getOnlineStatus(String id);
    String getDisplayName(String id);
    String getBio(String userId);

    void setDisplayName(String userId, String change);
}
