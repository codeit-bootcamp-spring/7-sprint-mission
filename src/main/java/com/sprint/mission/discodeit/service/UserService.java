package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.VerifiedUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface UserService {
    User create(User user);
    User get(UUID uuid);
    List<User> getAll();
    User update(User user);
    boolean delete(UUID uuid);
    User setUserState(UUID uuid, UserState userState);
    List<User> getUsersByName(String username);
    List<User> getUsersByEmail(String email);
    List<User> getUsersByState(UserState userState);
    User login(String email, String password);
    User logout(String email);
}
