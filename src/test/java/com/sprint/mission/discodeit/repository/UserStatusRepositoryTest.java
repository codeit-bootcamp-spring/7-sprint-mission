package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest // JPA TEST, Transactional 내장
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2로 교체하지 않고 postgresql로 테스트
class UserStatusRepositoryTest {

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("UserStatus 생성 테스트")
  void givenUserStatus_whenCreate_thenSuccess() {
    // given
    User user = new User("Test1", "test1@codeit.com", "test1234", null);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusRepository.save(userStatus);
    // when
    UserStatus byId = userStatusRepository.findById(userStatus.getId()).orElse(null);

    // then
    // UserStatus가 생성되면, 처음은 ONLINE 상태
    assertEquals(UserStatusType.ONLINE, byId.isOnline());
  }

  /***
   * 설계는 5분이 지나야, inOnline이 OFFLINE이 변경되지만
   * 테스트 상, 현 시간부터 -5분 하도록 해서 테스트
   */
  @Test
  @DisplayName("UserStatus type OFFLINE으로 바꿔서 테스트")
  void givenUserStatus_WhenOFFLINE_thenSuccess() {
    // given
    User user = new User("Test1", "test1@codeit.com", "test1234", null);
    userRepository.save(user);

    // when
    Instant fiveMinutesAgo = Instant.now().minusSeconds(350);
    UserStatus userStatus = new UserStatus(user, fiveMinutesAgo);
    userStatusRepository.save(userStatus);

    UserStatus byId = userStatusRepository.findById(userStatus.getId()).orElse(null);

    // then
    assertEquals(UserStatusType.OFFLINE, byId.isOnline());
  }

  @Test
  @DisplayName("유저 삭제 시 연관된 UserStatus 삭제 테스트")
  void givenUserWithUserStatus_whenDeleteUser_thenUserStatusIsDeleted() {
    // given
    User user = new User("Test1", "test1@codeit.com", "test1234", null);
    UserStatus userStatus = new UserStatus(user, Instant.now());
    userRepository.flush();
    // when
    userRepository.delete(user);

    // then
    assertEquals(0, userRepository.count());
    assertEquals(0, userStatusRepository.count()); // UserStatus도 삭제됨
  }

  @Test
  @DisplayName("UserStatus 삭제, 유저 테스트")
  void givenUserWithUserStatus_whenDeleteUserStatus_thenUserRemain() {
    // given
    User user = new User("Test1", "test1@codeit.com", "test1234", null);
    UserStatus userStatus = new UserStatus(user, Instant.now());
    userRepository.save(user);
    userStatusRepository.save(userStatus);
    userRepository.flush();
    // when
    userStatusRepository.delete(userStatus);

    // then
    assertEquals(1, userRepository.count());       // User는 삭제되지 않음
    assertEquals(0, userStatusRepository.count()); // UserStatus만 삭제됨
  }
}