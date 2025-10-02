package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*; // Map, HashMap, List, ArrayList, UUID ((java.util.;에 대한 모든 것 사용 가능))

public class JCFUserService implements UserService {

    public JCFUserService() {
        this.data = new LinkedHashMap<>();
    }

    private final Map<UUID, User> data;

    @Override
    public User create(String username, String email) {
        User user = new User(username, email);  // 새로운 유저 등록을 위해 User 객체 생성
        data.put(user.getId(), user);   // 생성된 유저를 맵에 저장 (key: UUID, value: User 객체)
        return user;    // 생성된 유저 객체 반환
    }


    // 특정 User 조회
    @Override

    public User read(UUID id) {
        return data.get(id);
    }


    // read all : 모든 User 조회
    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }


    // update : 유저 네임 변경
    @Override
    public User updateUsername(UUID id, String newUsername) {
        User u = data.get(id);
        if (u != null) {
            u.updateUsername(newUsername);
        }
        return u;
    }


    // update : 이메일 변경
    @Override
    public User updateEmail(UUID id, String newEmail) {
       User u = data.get(id);
       if (u != null) {
              u.updateEmail(newEmail);
       }
        return u;
    }


    // 유저 비활성화
    @Override
    public User deactivate(UUID id) {
        User u = data.get(id);
        if (u != null) {
            u.deactivate();
        }
        return u;
    }


    // 유저 삭제
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}
