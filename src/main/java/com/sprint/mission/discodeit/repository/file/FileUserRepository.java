package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;

public class FileUserRepository extends BaseFileRepository<User> implements UserRepository {
    //싱글톤 구현
    private final static FileUserRepository userRepository = new FileUserRepository();

    private FileUserRepository(){
        super(User.class);
    }

    public static FileUserRepository getInstance(){
        return userRepository;
    }
    
    //저장
    @Override
    public User save(User user) {
        saveToFile(user.getId(), user);
        return user;
    }

    //유저 목록
    @Override
    public List<User> findAll() {
        return findAllFiles();
    }

    //유저 id로 조회
    @Override
    public User findById(UUID id) {
        return loadFromFile(id);
    }

    //유저 email로 조회
    @Override
    public User findByEmail(String email) {
        return findAllFiles().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    //유저 nickname으로 조회
    @Override
    public List<User> findByNickname(String nickname) {
        return findAllFiles().stream()
                .filter(u -> u.getEmail().equals(nickname))
                .toList();
    }

    //유저 수정
    @Override
    public User update(UUID userId, String nickname, String password) {
        User user = loadFromFile(userId);
        if(user == null){
            throw new RuntimeException("User with id=" + userId + " not found");
        }
        user.update(nickname, password);
        saveToFile(userId, user);
        return user;
    }

    //유저 삭제
    @Override
    public User delete(UUID userId) {
        User user = loadFromFile(userId);
        deleteFile(userId);
        return user;
    }
}
