package com.sprint.mission.discodeit.repository.JCF;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;

import java.util.*;

//@Repository
public class JCFUserRepository implements InterfaceUserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository()  {
        this.data = new HashMap<>();
    }

    @Override
    public void save(User user) {
        this.data.put(user.getId(), user);
//        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public Optional<List<User>> findAll() {
        List<User> list = this.data.values().stream().toList();
        return Optional.ofNullable(list);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }

    @Override
    public boolean isUsingName(String name) {
        List<User> list = this.data.values().stream().toList();
        return list.stream().anyMatch(user -> user.getUserName().equals(name));
    }

    @Override
    public boolean isUsingEmail(String eMail) {
//        return findAll().stream().anyMatch(user -> user.getEMail().equals(eMail));
        return false;
    }

    @Override
    public boolean existsByName(String thisName) {
//        return findAll().stream().anyMatch(user -> user.getUserName().equals(name));
        return false;
    }

    @Override
    public Res_UserLogin isLogin(String name, String password) {
        // findAll() 인데 이걸로 안되서 그냥 중복으로.. ㅠㅠ
        List<User> users = this.data.values().stream().toList();
        User user1 = users.stream().filter(user -> user.getUserName().equals(name) && user.getPassword().equals(password)).findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        return Res_UserLogin.from(user1);
    }
}
