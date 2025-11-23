package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository extends BaseJCFRepository<User> implements UserRepository {

  // 저장
  @Override
  public User save(User user) {
    beforeModify();
    data.put(user.getId(), user);
    return user;
  }

  //유저 목록
  @Override
  public List<User> findAll() {
    return data.values().stream().toList();
  }

  //유저 Id
  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  //유저 email
  @Override
  public Optional<User> findByEmail(String email) {
    return findAll().stream()
        .filter(u -> u.getEmail().equals(email))
        .findFirst();
  }

  //유저 nickname
  @Override
  public Optional<User> findByNickname(String nickname) {
    return findAll().stream()
        .filter(u -> u.getNickname().equals(nickname))
        .findFirst();
  }

  @Override
  public void update(UUID userId, String email, String nickname, String password) {
    beforeModify();
    data.get(userId).update(email, nickname, password);
  }

  @Override
  public void updateProfileImage(UUID userId, UUID profileId) {
    beforeModify();
    data.get(userId).updateProfile(profileId);
  }

  @Override
  public void updatePasswordTemporary(UUID userId, String password) {
    beforeModify();
    data.get(userId).updateTemporaryPassword(password);
  }

  @Override
  public void delete(UUID userId) {
    beforeModify();
    data.remove(userId);
  }

  @Override
  public boolean existsByEmail(String email) {
    return false;
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return false;
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public boolean existsByEmailAndIdNot(String email, UUID id) {
    return data.values().stream().anyMatch(
        user -> user.getEmail().equals(email) &&
            !user.getId().equals(id));
  }

  @Override
  public boolean existsByNicknameAndIdNot(String nickname, UUID id) {
    return data.values().stream().anyMatch(
        user -> user.getNickname().equals(nickname) &&
            !user.getId().equals(id)
    );
  }
}
