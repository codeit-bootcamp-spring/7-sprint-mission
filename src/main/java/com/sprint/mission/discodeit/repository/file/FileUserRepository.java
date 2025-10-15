package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.nio.file.Path;
import java.util.*;
/**
 * ✅ FileUserRepository
 *  - User 데이터를 파일에 저장/로드하는 역할
 *  - AbstractFileMapRepository 상속으로 공통 로직 재사용
 */
public class FileUserRepository extends AbstractFileMapRepository<UUID, User>
        implements UserRepository {
    /**
     * 생성자: 파일 경로를 받아서, 기존 데이터가 있으면 로드함
     */
    // 기본 생성자: FilePaths 상수 사용 (data/users.dat)
    public FileUserRepository() { super(FilePaths.USERS); }
    // 선택 생성자: 테스트 시 임시 경로를 주입할 때 사용 가능
    public FileUserRepository(Path path) { super(path); }

    @Override public User save(User e) {
        data.put(e.getId(), e);
        persist();  // 파일에 저장
        return e;
    }
    @Override public User findById(UUID id) { return data.get(id); }
    @Override public List<User> findAll() { return new ArrayList<>(data.values()); }
    @Override public boolean deleteById(UUID id) {
        boolean removed = (data.remove(id) != null);
        if (removed) persist(); // 삭제 후 저장
        return removed;
    }
}
