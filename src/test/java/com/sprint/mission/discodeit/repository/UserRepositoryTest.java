package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUsername 성공: 저장된 유저를 username으로 조회")
    void findByUsername_success() {
        // given
        User save = userRepository.save(
                new User("user", "password123", "user@naver.com", null, UserRole.USER));

        // when
        List<User> result = userRepository.findByUsername("user");

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isNotNull();
        assertThat(result.get(0).getId()).isEqualTo(save.getId());
        assertThat(result.get(0).getUsername()).isEqualTo("user");
        assertThat(result.get(0).getEmail()).isEqualTo("user@naver.com");


    }

    @Test
    @DisplayName("findByUsername 실패: 존재하지 않은 username이면 리스트 반환")
    void findByUsername_fail() {
        // when
        List<User> result = userRepository.findByUsername("user");

        // then
        assertThat(result).isEmpty();

    }

    @Test
    @DisplayName("findByEmail 성공: 저장된 유저를 email로 조회")
    void findByEmail_success() {
        // given
        userRepository.save(new User("user", "password1234", "user@naver.com", null, UserRole.USER));

        // when
        Optional<User> result = userRepository.findByEmail("user@naver.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotNull();
        assertThat(result.get().getUsername()).isEqualTo("user");
        assertThat(result.get().getEmail()).isEqualTo("user@naver.com");

    }

    @Test
    @DisplayName("findByEmail 실패: 존재하지 않는 email이면 empty 반환")
    void findByEmail_fail() {
        // when
        Optional<User> result = userRepository.findByEmail("user@naver.com");

        // then
        assertThat(result).isEmpty();

    }
}
