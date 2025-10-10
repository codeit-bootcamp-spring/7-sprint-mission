package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    //자바에서 final이 붙는 건 참조 자체를 변경할 수 없게 만든다는 뜻
    //그래서 리스트 안에 있는 내용은 수정 가능
    private final Map<UUID, User> store = new HashMap<>();
    private final Map<String, UUID> store2 = new HashMap<>();

    public void save(User user){
        UUID key = user.getId();
        store.put(key, user);
        String key2 = user.getEmail();
        store2.put(key2, key);
    }

    public void remove(User user){
        UUID userId = user.getId();
        store.remove(userId);
        String key2 = user.getEmail();
        store2.remove(key2);
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }


    public Optional<User> findByEmail(String email){
        UUID uuid = store2.get(email);
        return Optional.ofNullable(store.get(uuid));
    }


    @Override
    public List<User> findAll() {
        return List.copyOf(store.values().stream().toList());
    }
}
