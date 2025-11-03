/*
package com.sprint.mission.discodeit.service.file;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private final BasicUserService basicUserService;

    public FileUserService() {
        this.basicUserService = new BasicUserService(new FileUserRepository());
    }
    public FileUserService(UserRepository userRepository) {
        this.basicUserService = new BasicUserService(userRepository);
    }

    @Override public User create(User user) {return basicUserService.create(user);}
    @Override public User get(UUID uuid) {return basicUserService.get(uuid);}
    @Override public List<User> getAll() {return basicUserService.getAll();}
    @Override public User update(User user) {return basicUserService.update(user);}
    @Override public boolean delete(UUID uuid) {return basicUserService.delete(uuid);}
    // Online/Offline 전환
    @Override public User setUserState(UUID uuid, UserState userState) {return basicUserService.setUserState(uuid, userState);}
    // 이름으로 조회
    @Override public List<User> getUsersByName(String username) {return basicUserService.getUsersByName(username);}
    // 이메일로 조회
    @Override public Optional<User> getUsersByEmail(String email) {return basicUserService.getUsersByEmail(email);}
    // 특정 상태만 조회
    @Override
    public List<User> getUsersByState(UserState userState) {return basicUserService.getUsersByState(userState);}
    // 로그인
    @Override public User login(String email, String password) {return basicUserService.login(email, password);}
    // 로그아웃
    @Override public User logout(String email) {return basicUserService.logout(email);}
}

 */
