package com.sprint.mssion.discodeit.service;

import com.sprint.mssion.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String username, String email, String phoneNumber, String pronoun);
    User getUserById(UUID uuid);
    List<User> getAllUsers();
    void updateUser(UUID uuid, String username, String email, String phoneNumber, String pronoun);
    void deleteUser(UUID uuid);
    boolean isExistsUser(UUID userId);

    void addChannelToUser(UUID userId, UUID channelId);
    void removeChannelFromAllUsers(UUID channelId);
}

/** 스프링부트 사람 -> 컨트롤러 ->() -> 서비스 -> 엔티티
 * 계산기 -> > reposiory(데이터를 저장하는 곳)->?엔티티(int a1, int a2, getter, setter) -> 서비스(더하기, 빼기, 나누기, 곱하기)
 */
