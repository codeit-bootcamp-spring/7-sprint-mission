package com.sprint.mission.discodeit.slice.jpa;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.config.SecurityConfig;
import com.sprint.mission.discodeit.dto.user.UserUpdateParams;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("UserRepository JPA Slice Test")
class UserRepositorySliceTest {

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("existsByEmail")
    class ExistsByEmail {

        @Test
        @DisplayName("[Success] 존재하면 true")
        void shouldReturnTrue_whenExists() {
            userRepository.saveAndFlush(User.create("name", "a@a.com", "pw", null));

            boolean result = userRepository.existsByEmail("a@a.com");

            assertTrue(result);
        }

        @Test
        @DisplayName("[Fail] 없으면 false")
        void shouldReturnFalse_whenNotExists() {
            boolean result = userRepository.existsByEmail("missing@a.com");

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("existsByUsername")
    class ExistsByUsername {

        @Test
        @DisplayName("[Success] 존재하면 true")
        void shouldReturnTrue_whenExists() {
            userRepository.saveAndFlush(User.create("u1", "u1@a.com", "pw", null));

            boolean result = userRepository.existsByUsername("u1");

            assertTrue(result);
        }

        @Test
        @DisplayName("[Fail] 없으면 false")
        void shouldReturnFalse_whenNotExists() {
            boolean result = userRepository.existsByUsername("missing");

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("findByUsername")
    class FindByUsername {

        @Test
        @DisplayName("[Success] username 정확하면 User 반환")
        void shouldReturnUser_whenCorrect() {
            userRepository.saveAndFlush(User.create("u2", "u2@a.com", "pw123", null));

            Optional<User> found = userRepository.findByUsername("u2");
            assertTrue(found.isPresent());
            assertEquals("u2@a.com", found.get().getEmail());
        }

        @Test
        @DisplayName("[Fail] 유저 username 없으면 empty")
        void shouldReturnEmpty_whenWrongPassword() {
            userRepository.saveAndFlush(User.create("u3", "u3@a.com", "pw123", null));
            Optional<User> found = userRepository.findByUsername("failName");

            assertTrue(found.isEmpty());
        }
    }

    @Nested
    @DisplayName("Paging & Sorting")
    class PagingAndSorting {

        @Test
        @DisplayName("[Success] findAll(pageable) 페이징 + createdAt DESC 정렬 검증")
        void findAll_shouldPageAndSortByCreatedAtDesc() throws Exception {
            userRepository.save(User.create("u1", "1@a.com", "pw", null));
            Thread.sleep(5);
            userRepository.save(User.create("u2", "2@a.com", "pw", null));
            Thread.sleep(5);
            userRepository.saveAndFlush(User.create("u3", "3@a.com", "pw", null));

            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<User> page = userRepository.findAll(pageable);

            assertEquals(2, page.getContent().size());
            assertTrue(page.hasNext());

            Instant first = page.getContent().get(0).getCreatedAt();
            Instant second = page.getContent().get(1).getCreatedAt();
            assertNotNull(first);
            assertNotNull(second);

            assertTrue(first.isAfter(second)); // DESC 정렬 검증
        }

        @Test
        @DisplayName("[Fail] 페이지 범위 벗어나면 empty")
        void findAll_shouldReturnEmpty_whenPageOutOfRange() {
            userRepository.saveAndFlush(User.create("u1", "only@a.com", "pw", null));

            Pageable pageable = PageRequest.of(2, 10); // 0-based
            Page<User> page = userRepository.findAll(pageable);

            assertTrue(page.getContent().isEmpty());
        }
    }

    @Nested
    @DisplayName("Auditing")
    class Auditing {

        @Test
        @DisplayName("[Infra] updatedAt이 저장/수정 시 갱신된다")
        void updatedAt_shouldBeSet_onPersistAndUpdate() throws Exception {
            User user = userRepository.save(User.create("name", "email@emao.com", "password", null));
            Instant first = user.getUpdatedAt();

            Thread.sleep(5);
            user.update(new UserUpdateParams("newNickname", null, null, null));
            userRepository.flush();

            Instant second = user.getUpdatedAt();

            assertNotNull(first);
            assertNotNull(second);
            assertTrue(second.isAfter(first));
        }
    }
}