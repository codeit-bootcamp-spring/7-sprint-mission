package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    // 데이터 저장 필드
    private final Map<UUID, User> users = new HashMap<>();

    // 싱글톤 패턴
    private static JCFUserService instance;
    private  JCFUserService() {}
    public static JCFUserService getInstance(){
        if (instance == null){
            instance = new JCFUserService();
        }
        return instance;
    }

    @Override
    public User createUser(String username, String nickName) {
        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("사용자명이 비어 있을 수 없습니다.");
        }
        if(nickName == null || nickName.trim().isEmpty()){
            throw new IllegalArgumentException("닉네임이 비어 있을 수 없습니다.");
        }
        User user = new User(username, nickName);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUser(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(String username, UUID userId, String nickName) {
        User user = users.get(userId);
        if(username != null && !username.isEmpty()){
            user.setUsername(username);
        }
        if(nickName != null && !nickName.isEmpty()){
            user.setNickName(nickName);
        }
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        users.remove(userId);
    }
}
