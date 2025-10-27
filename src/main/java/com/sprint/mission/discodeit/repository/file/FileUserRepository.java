package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

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

@Repository
public class FileUserRepository implements UserRepository {
    private final Map<UUID, User> userStore = new HashMap<>();
    private final String filePath = AppConfig.DATA_PATH + "\\users.sav";

    private FileUserRepository() {
        loadUsersFromFile();
    }

    private static FileUserRepository instance = new FileUserRepository();

    public static FileUserRepository getInstance() {
        return instance;
    }

    // 저장하기
    private void saveUsersToFile() {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(userStore);
            System.out.println("✅ 사용자 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 불러오기
    private void loadUsersFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("ℹ️ 저장된 사용자 파일이 없어 새로 생성합니다.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Map<UUID, User> loaded = (Map<UUID, User>) ois.readObject();
            userStore.clear();
            userStore.putAll(loaded);
            System.out.println("✅ 사용자 정보를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ 사용자 정보 불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void save(User user) {
        userStore.put(user.getId(), user);
        saveUsersToFile();
    }

    @Override
    public User findById(UUID id) {
        if(!isExist(id)){
            throw new IllegalArgumentException("해당 UUID를 가진 유저가 존재하지 않습니다.");
        }

        // 해당 key(UUID) 값에 value(유저)가 null로 저장되어 있는 경우 예외 발생
        return Optional.ofNullable(userStore.get(id))
                .orElseThrow(() -> new IllegalArgumentException("해당 UUID를 가진 유저가 존재하지 않습니다."));
    }

    @Override
    public User findByEmail(String email) {
        return userStore.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 사용하는 유저가 존재하지 않습니다."));
    }

    @Override
    public User findByPhone(String phoneNum) {
        return userStore.values().stream()
                .filter(u -> u.getPhoneNum().equals(phoneNum))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 전화번호를 사용하는 유저가 존재하지 않습니다."));
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userStore.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public void update(User user) {
        if(isExist(user.getId())){
            userStore.replace(user.getId(), user);
            saveUsersToFile();
        } else {
            throw new IllegalArgumentException("해당 유저가 존재하지 않아 정보 수정에 실패하였습니다.");
        }
    }

    @Override
    public void deleteById(UUID id) {
        if(!isExist(id)) {
            throw new IllegalArgumentException("삭제할 유저가 존재하지 않습니다.");
        }
        userStore.remove(id);
        saveUsersToFile();
    }

    @Override
    public boolean isExist(UUID id) {
        return userStore.containsKey(id);
    }
}
