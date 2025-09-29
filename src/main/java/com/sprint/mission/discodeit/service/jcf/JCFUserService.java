package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

/**
 * UserService의 JCF(Java Collections Framework) 기반 구현체
 * 내부적으로 HashMap을 사용하여 User 데이터를 메모리에 저장한다.
 */
public class JCFUserService implements UserService {
    private final Map<UUID, User> data; // User 데이터를 저장하는 Map

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    /**
     * User 생성 (등록)
     */
    @Override
    public User create(User user) {
        data.put(user.getId(), user);
        return user;
    }

    /**
     * User 단건 조회
     */
    @Override
    public User read(UUID id) {
        return data.get(id);
    }

    /**
     * 전체 User 조회
     */
    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * User 수정 (기존 id에 해당하는 데이터 교체)
     */
    @Override
    public User update(UUID id, User user) {
        if (data.containsKey(id)) {
            data.put(id, user);
            return user;
        }
        return null; // 수정 실패 (id 없음)
    }

    /**
     * User 삭제
     */
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}