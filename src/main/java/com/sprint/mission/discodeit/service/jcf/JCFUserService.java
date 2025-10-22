package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

/**
 * ✅ JCFUserService
 * - UserService 인터페이스의 구현체
 * - Java Collections Framework(Map)를 사용하여 User 데이터를 메모리에 저장한다.
 *
 * 🔹 구조
 *   key: UUID (User의 고유 id)
 *   value: User 객체
 *
 * 🔹 제공 기능
 *   - create / read / readAll / updateUsername / updateEmail / delete
 *   - activate / deactivate
 */
public class JCFUserService implements UserService {

    /**
     * 메모리 저장소 (UUID → User)
     * final: 다른 Map 인스턴스로 변경 불가
     */
    private final Map<UUID, User> data;

    /**
     * 기본 생성자
     * - LinkedHashMap 사용 → 삽입 순서 유지 (readAll() 보기 좋게)
     */
    public JCFUserService() {
        this.data = new LinkedHashMap<>();
    }

    /**
     * CREATE
     * - 새로운 User를 생성해 저장소에 추가한다.
     */
    @Override
    public User create(String username, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username은 비어있을 수 없습니다.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email은 비어있을 수 없습니다.");
        }

        User user = new User(username, email, true); // 기본값: active = true
        data.put(user.getId(), user);
        return user;
    }

    /**
     * READ (단건)
     * - id(UUID)로 User를 조회한다.
     */
    @Override
    public User read(UUID id) {
        return data.get(id);
    }

    /**
     * READ ALL
     * - 모든 User를 반환한다.
     */
    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * UPDATE - 이름 수정
     * - 존재하는 사용자의 username을 변경한다.
     */
    @Override
    public User updateUsername(UUID id, String newName) {
        User user = data.get(id);
        if (user != null && newName != null && !newName.isBlank()) {
            user.updateUsername(newName); // 엔티티의 메서드로 처리
        }
        return user;
    }

    /**
     * UPDATE - 이메일 수정
     * - 존재하는 사용자의 email을 변경한다.
     */
    @Override
    public User updateEmail(UUID id, String newEmail) {
        User user = data.get(id);
        if (user != null && newEmail != null && !newEmail.isBlank()) {
            user.updateEmail(newEmail);
        }
        return user;
    }

    /**
     * DELETE
     * - 해당 사용자를 삭제한다.
     */
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    /**
     * ACTIVATE
     * - 비활성화된 사용자를 다시 활성화한다.
     */
    @Override
    public User activate(UUID id) {
        User user = data.get(id);
        if (user != null) {
            user.setActive(true);
        }
        return user;
    }

    /**
     * DEACTIVATE
     * - 사용자를 비활성화한다.
     */
    @Override
    public User deactivate(UUID id) {
        User user = data.get(id);
        if (user != null) {
            user.setActive(false);
        }
        return user;
    }
}