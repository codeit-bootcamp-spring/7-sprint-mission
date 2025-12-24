package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.config.QuerydslConfig;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test") // active 설정이 따로 있으면 넣어줘야 함
@Import(QuerydslConfig.class)
@DataJpaTest
@DisplayName("userRepository 테스트")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 중복 체크 성공")
    void existsByUsernameAndEmail() {
        // given
        User user = new User(
                "test@gmail.com",
                "password",
                "testUser"
        );
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmailOrUsername(
                "another@email.com",
                "testUser"
        );

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("username으로 유저 조회 성공")
    void findByUsername_success() {
        // given
        User user = new User(
                "test@gmail.com",
                "1234",
                "testUser"
        );
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByUsername("testUser");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testUser");
        assertThat(result.get().getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    @DisplayName("username으로 유저 조회 성공")
    void findByUsername_notFound() {
        // when
        Optional<User> result = userRepository.findByUsername("no-user");

        // then
        assertThat(result).isEmpty();
    }
}
