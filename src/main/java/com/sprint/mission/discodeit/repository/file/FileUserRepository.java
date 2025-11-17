package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository extends BaseFileRepository<User> implements UserRepository {

  public FileUserRepository(RepositoryProperties repositoryProperties) {
    super(User.class, repositoryProperties);
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
  public Optional<User> findById(UUID id) {
    return loadFromFile(id);
  }

  //유저 email로 조회
  @Override
  public Optional<User> findByEmail(String email) {
    return findAllFiles().stream()
        .filter(u -> u.getEmail().equals(email))
        .findFirst();
  }

  //유저 nickname으로 조회
  @Override
  public Optional<User> findByNickname(String nickname) {
    return findAllFiles().stream()
        .filter(u -> u.getNickname().equals(nickname))
        .findFirst();
  }

  //유저 수정
  @Override
  public void update(UUID userId, String email, String nickname, String password) {
    loadFromFile(userId).ifPresent(user -> {
      user.update(email, nickname, password);
      saveToFile(userId, user);
    });
  }

  @Override
  public void updateProfileImage(UUID userId, UUID profileId) {
    loadFromFile(userId).ifPresent(user -> {
      user.updateProfile(profileId);
      saveToFile(userId, user);
    });
  }

  //유저 삭제
  @Override
  public void delete(UUID userId) {
    deleteFile(userId);
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

  @Override
  public boolean existsById(UUID id) {
    return fileExistsById(id);
  }

  @Override
  public boolean existsByEmailAndIdNot(String email, UUID id) {
    return findAllFiles().stream().anyMatch(
        user -> user.getEmail().equals(email) &&
            !user.getId().equals(id));
  }

  @Override
  public boolean existsByNicknameAndIdNot(String nickname, UUID id) {
    return findAllFiles().stream().anyMatch(
        user -> user.getNickname().equals(nickname) &&
            !user.getId().equals(id)
    );
  }
}
