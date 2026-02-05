package com.sprint.mission.discodeit.unit.entity;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User newUser() {
        return User.create("name", "example@email.com", "password123", null);
    }

    @Nested
    @DisplayName("User 생성")
    class ConstructorInvariant {

        @Test
        @DisplayName("[Invariant][Negative] 필수입력값 유효하지않으면 예외")
        void constructor_shouldThrowException_whenRequiredFieldsInvalid() {
            assertThrows(IllegalArgumentException.class,
                    () -> User.create(null, null, null, null));
            assertThrows(IllegalArgumentException.class,
                    () -> User.create(null, "a@b.com", "pw", null));
            assertThrows(IllegalArgumentException.class,
                    () -> User.create("nick", "invalid", "pw", null));
            assertThrows(IllegalArgumentException.class,
                    () -> User.create("nick", "a@b.com", "", null));
        }


        @Test
        @DisplayName("[Invariant][Negative] 이메일 형식이 잘못되면 예외")
        void constructor_shouldThrow_whenEmailInvalid() {
            assertThrows(IllegalArgumentException.class,
                    () -> User.create("name", "invalid", "pw", null));
        }

        @Test
        @DisplayName("[Invariant][Positive] 유효한 인자면 정상 생성")
        void constructor_shouldCreate_whenValid() {
            User u = newUser();

            assertEquals("name", u.getUsername());
            assertEquals("example@email.com", u.getEmail());
            assertEquals("password123", u.getPassword());
            assertNull(u.getId()); // 실제 persist 전이면 @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID) 이기떄문에 단위테스트에서는 Null
            assertNull(u.getCreatedAt()); // 실제 persist 전이면  @CreatedDate 이기떄문에 단위테스트에서는 Null
            assertNull(u.getUpdatedAt()); // 실제 persist 전이면  @LastModifiedDate 이기떄문에 단위테스트에서는 Null
        }
    }


    @Nested
    @DisplayName("User 닉네임 변경 규칙")
    class NicknameRule {
        @Test
        @DisplayName("[Rule][Positive] 유효한 닉네임이면 변경된다")
        void updateNickname_shouldChange_whenValid() {
            User u = newUser();
            boolean changed = u.updateNickname("nick2");

            assertTrue(changed);
            assertEquals("nick2", u.getUsername());
        }

        @Test
        @DisplayName("[Rule][Negative] 닉네임이 공백이면 변경되지 않는다")
        void updateNickname_shouldNotChange_whenBlank() {
            User u = newUser();
            assertFalse(u.updateNickname(""));
            assertEquals("name", u.getUsername());
        }

        @Test
        @DisplayName("[Rule][Negative] 닉네임이 null이면 변경되지 않는다")
        void updateNickname_shouldNotChange_whenNull() {
            User u = newUser();
            assertFalse(u.updateNickname(null));
            assertEquals("name", u.getUsername());
        }

        @Test
        @DisplayName("[Rule][Negative] 닉네임이 동일하면 변경되지 않는다")
        void updateNickname_shouldNotChange_whenSame() {
            User u = newUser();
            assertFalse(u.updateNickname("name"));
            assertEquals("name", u.getUsername());
        }
    }

    @Nested
    @DisplayName("User 이메일 변경 규칙")
    class EmailRule {
        @Test
        @DisplayName("[Rule][Positive] 이메일이 유효하고 기존과 다르면 변경된다")
        void updateEmail_shouldChange_whenValidAndDifferent() {
            User u = newUser();
            assertTrue(u.updateEmail("b@c.com"));
            assertEquals("b@c.com", u.getEmail());
        }

        @Test
        @DisplayName("[Rule][Negative] 이메일이 null/blank면 변경되지 않는다 (형식 검사는 생성 시에만)")
        void updateEmail_shouldNotChange_whenNullOrBlank() {
            User u = newUser();
            assertFalse(u.updateEmail(null));
            assertEquals("example@email.com", u.getEmail());

            assertFalse(u.updateEmail(""));
            assertEquals("example@email.com", u.getEmail());
        }

        @Test
        @DisplayName("[Rule][Negative] 이메일이 동일하면 변경되지 않는다")
        void updateEmail_shouldNotChange_whenSame() {
            User u = newUser();
            assertFalse(u.updateEmail("example@email.com"));
            assertEquals("example@email.com", u.getEmail());
        }
    }

    @Nested
    @DisplayName("User 비밀번호 변경 규칙")
    class PasswordRule {
        @Test
        @DisplayName("[Rule][Positive] 유효하고 기존과 다르면 변경된다")
        void updatePassword_shouldChange_whenValidAndDifferent() {
            User u = newUser();
            assertTrue(u.updatePassword("pw2"));
            assertEquals("pw2", u.getPassword());
        }

        @Test
        @DisplayName("[Rule][Negative] 비밀번호가 null/blank면 변경되지 않는다")
        void updatePassword_shouldNotChange_whenNullOrBlank() {
            User u = newUser();
            assertFalse(u.updatePassword(null));
            assertEquals("password123", u.getPassword());

            assertFalse(u.updatePassword(""));
            assertEquals("password123", u.getPassword());
        }

        @Test
        @DisplayName("[Rule][Negative] 비밀번호가 동일하면 변경되지 않는다")
        void updatePassword_shouldNotChange_whenSame() {
            User u = newUser();
            assertFalse(u.updatePassword("password123"));
            assertEquals("password123", u.getPassword());
        }
    }
}