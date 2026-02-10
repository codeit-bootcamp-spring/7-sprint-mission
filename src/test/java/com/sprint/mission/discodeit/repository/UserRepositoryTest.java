package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("UserRepository Test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("검색 관련 기능")
    class SearchUser {

        @Test
        @DisplayName("성공: 이메일로 조회할 수 있다.")
        void findByEmail_Success() {
            // given
            userRepository.save(new User("user1@naver.com", "Qwer1234!", "user1", Role.USER));
            userRepository.save(new User("user2@naver.com", "Asdf1234!", "user2", Role.USER));

            // when
            Optional<User> found = userRepository.findByEmail("user1@naver.com");

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getUsername()).isEqualTo("user1");
        }

        @Test
        @DisplayName("실패: 존재하지 않는 이메일")
        void findByUsername_NotFoundEmail() {
            // given

            // when
            Optional<User> found = userRepository.findByEmail("user@naver.com");

            // then
            assertThat(found).isNotPresent();
        }
    }

    @Nested
    @DisplayName("직접 작성한 JPQL 검증")
    class ManualQuery {

        @Test
        @DisplayName("프로필과 상태로 조회한다.")
        void findAllWithProfileAndStatus() {
            // given
            userRepository.save(new User("user1@naver.com", "Qwer1234!", "user1", Role.USER));
            userRepository.save(new User("user2@naver.com", "Asdf1234!", "user2", Role.USER));

            // when
            List<User> users = userRepository.findAllWithProfile();

            // then
            assertThat(users).hasSize(2);
            assertThat(users.get(0).getUsername()).isEqualTo("user1");
            assertThat(users.get(1).getUsername()).isEqualTo("user2");

        }
    }
}