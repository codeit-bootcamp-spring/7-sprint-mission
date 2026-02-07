package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    private User user1;
    private User user2;
    private BinaryContent profile;
    private BinaryContent profile2;


    @BeforeEach
    void setUp() {
        profile = BinaryContent.builder()
                .fileName("test1.jpg")
                .contentType("image/jpeg")
                .size(1L)
                .build();
        profile2 = BinaryContent.builder()
                .fileName("test1.jpg")
                .contentType("image/jpeg")
                .size(1L)
                .build();

        user1 = new User("user1", "user1@codiet.com", "user1", profile);
        user2 = new User("user2", "user2@codiet.com", "user2", profile2);
    }

    @Nested
    @DisplayName("유저 검색")
    class UserFind {
        @Test
        @DisplayName("[정상 케이스] - 이름 검색")
        void findByName_success() {
            // given
            userRepository.save(user1);
            entityManager.flush();
            entityManager.clear();


            // when
            User result = userRepository.findByUsername(user1.getUsername()).orElse(null);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo(user1.getUsername());
            assertThat(result.getId()).isEqualTo(user1.getId());
        }

        @Test
        @DisplayName("[정상 케이스] - 이메일 검색")
        void findByEmail_success() {
            // given
            userRepository.save(user1);
            entityManager.flush();
            entityManager.clear();

            // when
            User result = userRepository.findByEmail(user1.getEmail()).orElse(null);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(user1.getEmail());
            assertThat(result.getUsername()).isEqualTo(user1.getUsername());
        }

        @Test
        @DisplayName("[정상 케이스] - 존재하지 않는 유저")
        void findByUsername_notFound() {
            userRepository.save(user1);
            // when
            User result = userRepository.findByUsername("empty user").orElse(null);

            // then
            Assertions.assertThat(result).isEqualTo(null);
        }

    }

    @Nested
    @DisplayName("유저 검색 - fetch Join")
    class UserJoin {
        @Test
        @DisplayName("[정상 케이스] - 유저와 프로필, status 조회 성공")
        void findAllWithProfileAndStatus_success() {
            // given
            userRepository.save(user1);
            userRepository.save(user2);
            entityManager.flush();
            entityManager.clear();

            // when
            List<User> result = userRepository.findAllWithProfile();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(User::getId).contains(user1.getId(), user2.getId());

            result.forEach(user -> {
                assertThat(user.getProfile()).isNotNull();
            });

        }

        @Test
        @DisplayName("[정상 케이스] 빈 리스트 반환 - 유저가 없는 경우")
        void findAllWithProfileAndStatus_emptyList() {
            // when
            List<User> result = userRepository.findAllWithProfile();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("[정상 케이스] Profile이 null인 유저도 조회됨 (LEFT JOIN)")
        void findAllWithProfileAndStatus_withNullProfile() {
            // given
            User userWithoutProfile = new User("user3", "user3@codeit.com", "password3", null);
            // profile은 설정하지 않음

            userRepository.save(userWithoutProfile);
            entityManager.flush();
            entityManager.clear();

            // when
            List<User> result = userRepository.findAllWithProfile();

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getProfile()).isNull();
        }
    }

}