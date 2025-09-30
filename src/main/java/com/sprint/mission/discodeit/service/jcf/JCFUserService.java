package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.update.UserUpdate;
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
    public User update(UUID id, UserUpdate uu) {
        if (uu.getUserName() != null && !uu.getUserName().isEmpty()) {
            users.get(id).updateUserName(uu.getUserName());
        }
        if (uu.getPassword() != null && !uu.getPassword().isEmpty()
                && !(uu.getPassword().length() < 8)) {
            users.get(id).updatePassword(uu.getPassword());
        }
        if (uu.getPhoneNum() != null && !uu.getPhoneNum().isEmpty()
                && !(uu.getPhoneNum().length() < 11)) {
            users.get(id).updatePhoneNum(uu.getPhoneNum());
        }
        return users.get(id);
    }

    @Override
    public User updateState(UUID id, User.State state) {
        users.get(id).updateState(state);
        return users.get(id);
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }
}

// https://velog.io/@27kanghan/Builder-Pattern%EC%9D%98-toBuilder
// https://adjh54.tistory.com/78