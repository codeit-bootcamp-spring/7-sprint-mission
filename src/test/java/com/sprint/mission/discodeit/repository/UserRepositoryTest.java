package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest // JPA TEST, Transactional 내장
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2로 교체하지 않고 postgresql로 테스트
// @DataJpaTest는 각 테스트가 트랜잭션 환경에서 실행되고 테스트 종료 시 flush가 발생한다.
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Test
  @DisplayName("유저 생성 테스트")
  void givenUser_whenCreate_thenSuccess() {
    // give
    User user = new User("Test1", "test1@codeit.com", "test1234", null);
    userRepository.save(user);

    // when
    User findUser = userRepository.findById(user.getId()).orElse(null);

    // then
    String username = findUser.getUsername();
    String email = findUser.getEmail();
    String password = findUser.getPassword();

    assertEquals(user.getUsername(), username);
    assertEquals(user.getEmail(), email);
    assertEquals(user.getPassword(), password);
  }


  // Profile을 가진 유저를 저장할 시, BinaryContents 테이블에도 profile이 저장되어야함.
  // BinaryContents에서 가져온 id와 저장된 User의 profile_id(fk)가 같아야 함을 보장
  @Test
  @DisplayName("유저 생성 테스트 profile 확인")
  void givenUserWithProfile_whenCreate_thenSuccess() {
    // given
    byte[] bytes = {0, 0};
    BinaryContent binaryContent = new BinaryContent("testFile", 10L, "*/*", bytes);

    User user = new User("Test1", "test1@codeit.com", "test1234", binaryContent);
    userRepository.save(user);

    // when
    User findUser = userRepository.findById(user.getId()).orElse(null);
    BinaryContent findBinaryContent = binaryContentRepository.findById(binaryContent.getId())
        .orElse(null);

    // then
    assertEquals(findUser.getProfile().getId(), findBinaryContent.getId());
  }

  @Test
  @DisplayName("중복 유저 테스트")
  void givenDuplicatedUser_whenFlush_thenFail() {
    // given
    User user1 = new User("Test1", "test1@codeit.com", "test1234", null);
    userRepository.save(user1);
    User duplicatedUser = new User("Test1", "test2@codeit.com", "test1234", null);
    userRepository.save(duplicatedUser);
    // when
    // then
    // flush를 통해 실제 DB에 데이터를 넣어서, UK가 위반되는지 확인
    assertThrows(DataIntegrityViolationException.class, () -> {
      userRepository.flush();
    });
  }

  @Test
  @DisplayName("삭제 테스트")
  void givenUserWithProfile_whenDeleteUser_thenProfileRemainss() {
    byte[] bytes = {0, 0};
    BinaryContent binaryContent = new BinaryContent("testFile", 10L, "*/*", bytes);
    User user = new User("Test1", "test1@codeit.com", "test1234", binaryContent);
    userRepository.save(user);

    // when
    userRepository.delete(user);
    userRepository.flush();

    // then
    // User를 삭제하게 되면, BinaryContent에 있는 해당 profile은 고아 객체로써 삭제
    assertEquals(0, userRepository.count());
    assertEquals(0, binaryContentRepository.count());

  }

  @Test
  @DisplayName("유저 변경 테스트")
  void givenUserWithProfile_whenUpdateUser_thenSuccess() {
    // given
    byte[] bytes = {0, 0};
    BinaryContent binaryContent = new BinaryContent("testFile", 10L, "*/*", bytes);
    binaryContentRepository.save(binaryContent);

    User user = new User("Test1", "test1@codeit.com", "test1234", binaryContent);
    User savedUser = userRepository.save(user);

    // when
    // 영속성 컨텍스트에 의해, save를 하지 않아도, 반영이 된다.
    //    - savedUser는 userRepository.save(user)를 통해 영속성 컨텍스트에 관리되는 엔티티가 됩니다.
    //    - 이후 savedUser.updateUser(...)로 필드를 변경하면, JPA는 **더티 체킹(dirty checking)**을 통해
    //      변경된 필드를 감지합니다.
    //    - 트랜잭션이 끝날 때 flush()가 호출되면서 변경 사항이 DB에 반영됩니다.
    //    - 따라서 save()를 다시 호출하지 않아도 findById(...)로 조회했을 때 변경된 값이 반영된 상태로 나옵니다.

    savedUser.updateUser("Test1Update", "test1@codeit.com", "test1234", binaryContent);
    User updateUser = userRepository.findById(user.getId()).orElse(null);

    // then
    assertEquals("Test1Update", updateUser.getUsername());
    assertEquals(updateUser.getId(), savedUser.getId());
  }

}