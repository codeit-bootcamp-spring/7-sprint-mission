package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.nio.file.Path;
import java.util.*;
/**
 * ✅ FileUserService
 *  - 비즈니스 로직 (검증, 상태 변경)을 담당
 *  - 실제 저장은 FileUserRepository를 통해 수행
 */
public class FileUserService implements UserService {

    private final UserRepository repo;

    /** 기본 파일 경로를 사용하는 기본 생성자: data/users.dat 경로의 Repository 사용 */
    public FileUserService() {
        this.repo = new FileUserRepository(Path.of("data/users.dat"));
    }

    /** 선택 생성자: (주입)테스트/확장용 — 경로 주입 가능 */
    public FileUserService(UserRepository repo) {
        this.repo = repo;
    }

    // === 비즈니스 로직 (유효성 검사 등) + 저장은 레포지토리에 위임 ===
    // === CRUD ===
    @Override
    public User create(String username, String email) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username 비어있음");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email 비어있음");
        User u = new User(username, email); // 기본 active=true 오버로딩 생성자 사용
        return repo.save(u);
    }

    @Override
    public User read(UUID id) { return repo.findById(id); }

    @Override
    public List<User> readAll() { return repo.findAll(); }

    @Override
    public User updateUsername(UUID id, String newName) {
        User u = repo.findById(id);
        if (u != null && newName != null && !newName.isBlank()) {
            u.updateUsername(newName);
            repo.save(u); // save로 갱신 반영
        }
        return u;
    }

    @Override
    public User updateEmail(UUID id, String newEmail) {
        User u = repo.findById(id);
        if (u != null && newEmail != null && !newEmail.isBlank()) {
            u.updateEmail(newEmail);
            repo.save(u);
        }
        return u;
    }

    @Override
    public boolean delete(UUID id) {
        return repo.deleteById(id);
    }

    @Override
    public User activate(UUID id) {
        User u = repo.findById(id);
        if (u != null) {
            u.setActive(true);
            repo.save(u);
        }
        return u;
    }

    @Override
    public User deactivate(UUID id) {
        User u = repo.findById(id);
        if (u != null) {
            u.setActive(false);
            repo.save(u);
        }
        return u;
    }
}