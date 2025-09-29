package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    Map<UUID, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        users.put(user.getId(), user);  // 유저 맵에 추가
        return user;
    }

    @Override
    public User read(UUID id) {
        return users.get(id);   // 유저 정보 출력
    }

    @Override
    public List<User> readAll() {
        List<User> userList = new ArrayList<>(users.values());  // users 전체출력
        userList.sort(Comparator.comparing(User::getCreatedAt));    // 생성일자 오름차순 정렬

        return userList;
    }

    @Override
    public User update(UUID id, User user) {
        return null;    // 뭘 업뎃해야함?
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }
}
