package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @TestConfiguration
    @EnableJpaAuditing
    static class TestAuditingConfig {
    }

    @Test
    @DisplayName("findByUsername 성공: username이 존재하면 User를 반환한다")
    void findByUsername_success() {
        User user = new User("taehun", "taehun@test.com", "password1234", null);
        userRepository.saveAndFlush(user);
        userStatusRepository.saveAndFlush(new UserStatus(user, Instant.now()));

        var found = userRepository.findByUsername("taehun");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("taehun@test.com");
    }

    @Test
    @DisplayName("findByUsername 실패: username이 없으면 empty를 반환한다")
    void findByUsername_failure() {
        var found = userRepository.findByUsername("nope");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("existsByEmail/existsByUsername 성공/실패")
    void exists_checks() {
        User user = new User("u1", "u1@test.com", "password1234", null);
        userRepository.saveAndFlush(user);
        userStatusRepository.saveAndFlush(new UserStatus(user, Instant.now()));

        assertThat(userRepository.existsByEmail("u1@test.com")).isTrue();
        assertThat(userRepository.existsByEmail("no@test.com")).isFalse();

        assertThat(userRepository.existsByUsername("u1")).isTrue();
        assertThat(userRepository.existsByUsername("nope")).isFalse();
    }

    @Test
    @DisplayName("findAllWithProfileAndStatus 성공: status가 있는 유저들을 profile 포함해서 조회한다")
    void findAllWithProfileAndStatus_success() {
        User user1 = new User("a", "a@test.com", "password1234", null);
        User user2 = new User("b", "b@test.com", "password1234", null);

        userRepository.saveAllAndFlush(List.of(user1, user2));
        userStatusRepository.saveAllAndFlush(List.of(
                new UserStatus(user1, Instant.now()),
                new UserStatus(user2, Instant.now())
        ));

        BinaryContent profile = BinaryContent.builder()
                .fileName("p.png")
                .contentType("image/png")
                .size(10L)
                .build();
        binaryContentRepository.saveAndFlush(profile);

        user1.update(null, null, null, profile);
        userRepository.saveAndFlush(user1);

        List<User> results = userRepository.findAllWithProfileAndStatus();

        assertThat(results).hasSize(2);
        assertThat(results).allSatisfy(u -> assertThat(u.getStatus()).isNotNull());
        assertThat(results.stream().map(User::getUsername)).containsExactlyInAnyOrder("a", "b");
    }

    @Test
    @DisplayName("페이징/정렬: username 내림차순으로 2개씩 조회된다")
    void paging_and_sorting() {
        User u1 = new User("a", "a2@test.com", "password1234", null);
        User u2 = new User("b", "b2@test.com", "password1234", null);
        User u3 = new User("c", "c2@test.com", "password1234", null);

        userRepository.saveAllAndFlush(List.of(u1, u2, u3));
        userStatusRepository.saveAllAndFlush(List.of(
                new UserStatus(u1, Instant.now()),
                new UserStatus(u2, Instant.now()),
                new UserStatus(u3, Instant.now())
        ));

        var page = userRepository.findAll(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "username")));

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getUsername()).isEqualTo("c");
        assertThat(page.getContent().get(1).getUsername()).isEqualTo("b");
    }
}
