package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class FileUserRepository extends BaseFileRepository<User> implements UserRepository {
    public FileUserRepository(){
        super(User.class);
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
    public User findByNickname(String nickname) {
        return findAllFiles().stream()
                .filter(u -> u.getNickname().equals(nickname))
                .findFirst().orElse(null);
    }

    //유저 수정
    @Override
    public User update(UUID userId,String email, String nickname, String password) {
        User user = loadFromFile(userId);
        if(user == null){
            throw new RuntimeException("User with id=" + userId + " not found");
        }
        user.update(email, nickname, password);
        saveToFile(userId, user);
        return user;
    }

    //유저 프로필 수정
    @Override
    public void updateProfileImg(UUID userId, UUID profileImgId) {
        User user = loadFromFile(userId);
        if(user == null){
            throw new RuntimeException("User with id=" + userId + " not found");
        }
        user.updateProfile(profileImgId);
        saveToFile(userId, user);
    }

    //유저 삭제
    @Override
    public User delete(UUID userId) {
        User user = loadFromFile(userId);
        deleteFile(userId);
        return user;
    }

    //이메일이 이미 존재하는지
    @Override
    public boolean existsByEmail(String email) {
        return findAllFiles().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    //닉네임이 이미 존재하는지
    @Override
    public boolean existsByNickname(String nickname) {
        return findAllFiles().stream()
                .anyMatch(u -> u.getNickname().equals(nickname));
    }
}
