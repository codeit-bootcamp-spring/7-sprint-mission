package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("유저 Repository 테스트")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Nested
  @DisplayName("조회 관련")
  class Search {

    @Test
    @DisplayName("유저 이름으로 유저를 찾을 수 있다")
    void findByUsername_Success() {
      // given
      userRepository.save(new User("진우", "a@a.com", "1234", null));

      // when
      Optional<User> foundUser = userRepository.findByUsername("진우");

      // then
      assertThat(foundUser).isPresent();
      assertThat(foundUser.get().getUsername()).isEqualTo("진우");
      assertThat(foundUser.get().getEmail()).isEqualTo("a@a.com");
    }

    @Test
    @DisplayName("존재하지 유저명 조회 시 Optional 반환")
    void findByUsername_NotFound() {
      // given
      String notUser = "빈 유저";

      // when
      Optional<User> foundUser = userRepository.findByUsername(notUser);

      // then
      assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("이메일로 유저를 찾을 수 있다")
    void findByEmail_Success() {
      // given
      userRepository.save(new User("진우", "a@a.com", "1234", null));

      // when
      Optional<User> foundEmail = userRepository.findByEmail("a@a.com");

      // then
      assertThat(foundEmail).isPresent();
      assertThat(foundEmail.get().getEmail()).isEqualTo("a@a.com");
    }

    @Test
    @DisplayName("존재하지 않은 이메일 유저 조회시 Optional 반환")
    void findByEmail_NotFound() {
      // given
      String notEmail = "b@b.com";

      // when
      Optional<User> byEmail = userRepository.findByEmail(notEmail);

      // then
      assertThat(byEmail).isEmpty();
    }
  }

}