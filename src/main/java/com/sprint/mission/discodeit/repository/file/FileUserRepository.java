package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

import static com.sprint.mission.discodeit.global.utils.FileIOHandler.*;

/**
 * FileUserRepository
 * -----------------
 * 파일 입출력(File I/O)을 통해 메시지를 영구적으로 저장하고 불러오는 구현체입니다.
 *
 * Java Collection Framework(JCF)를 기반으로 메시지를 메모리(List<User>)에 관리하며,
 * 변경 사항이 발생할 때마다 .sav 파일로 직렬화하여 데이터를 저장합니다.
 *
 * 프로그램 재시작 시 .sav 파일을 역직렬화(deserialize)하여 메시지 데이터를 복원합니다.
 *
 * 주요 기능:
 * - 유저 생성, 조회, 삭제 등의 기본 CRUD 지원
 * - 파일 기반 직렬화/역직렬화를 통한 데이터 지속성(persistence) 보장
 *
 * 사용 파일:
 * - users.sav : 메시지 객체들이 직렬화되어 저장되는 파일
 */
public class FileUserRepository implements UserRepository {
    private final Map<UUID, User> userStore = new HashMap<>();
    private final String filePath;

    public FileUserRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile(filePath, userStore);
    }

    @Override
    public void save(User user) {
        userStore.put(user.getId(), user);
        saveToFile(filePath, userStore);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userStore.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByPhone(String phoneNum) {
        return userStore.values().stream()
                .filter(u -> u.getPhoneNum().equals(phoneNum))
                .findFirst();
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return userStore.values().stream()
                .filter(u -> u.getLoginId().equals(loginId))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public void update(User user) {
        userStore.replace(user.getId(), user);
        saveToFile(filePath, userStore);
    }

    @Override
    public void deleteById(UUID id) {
        userStore.remove(id);
        saveToFile(filePath, userStore);
    }

    @Override
    public boolean isExist(UUID id) {
        return userStore.containsKey(id);
    }

    @Override
    public boolean existsByNickName(String NickName) {
        return findAll().stream().anyMatch(u -> u.getNickName().equals(NickName));
    }

    @Override
    public boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
