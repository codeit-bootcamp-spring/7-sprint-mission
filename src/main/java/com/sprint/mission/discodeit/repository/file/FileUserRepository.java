package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final List<User> userStore = new ArrayList<>();
    private final String USER_FILE;

    public FileUserRepository(String USER_FILE) {
        this.USER_FILE = USER_FILE;
        loadUsersFromFile();
    }

    // 저장하기
    private void saveUsersToFile() {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(userStore);
            System.out.println("✅ 사용자 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 불러오기
    private void loadUsersFromFile() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("ℹ️ 저장된 사용자 파일이 없어 새로 생성합니다.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            List<User> loaded = (List<User>) ois.readObject();
            userStore.clear();
            userStore.addAll(loaded);
            System.out.println("✅ 사용자 정보를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ 사용자 정보 불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void save(User user) {
        userStore.add(user);
        saveUsersToFile();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userStore.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userStore.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<User> findByPhone(String phoneNum) {
        return userStore.stream().filter(u -> u.getPhoneNum().equals(phoneNum)).findFirst();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userStore.stream().filter(u -> u.getUserId().equals(userId)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore);
    }

    @Override
    public void update(User user) {
        for (int i = 0; i < userStore.size(); i++) {
            if (userStore.get(i).getId().equals(user.getId())) {
                userStore.set(i, user);
                saveUsersToFile();
                break;
            }
        }
    }

    @Override
    public void deleteById(UUID id) {
        userStore.removeIf(u -> u.getId().equals(id));
        saveUsersToFile();
    }
}
