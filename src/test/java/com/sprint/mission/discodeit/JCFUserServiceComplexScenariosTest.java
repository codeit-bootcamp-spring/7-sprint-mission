package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFEventService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * User Service에서 발생할 수 있는 더 복합적인 시나리오와 엣지 케이스를 테스트합니다.
 */
@DisplayName("JCFUserService 복합 시나리오 테스트")
class JCFUserServiceComplexScenariosTest {

    private JCFUserService userService;
    private JCFUserRepository userRepository;
    private JCFEventService eventService;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        eventService = new JCFEventService();
        userService = new JCFUserService(userRepository, eventService);
    }

    @Nested
    @DisplayName("논리적 삭제(Soft Delete)와 다른 기능이 얽혔을 때")
    class WhenUserIsSoftDeleted {

        private User deletedUser;
        private UUID deletedUserId;

        @BeforeEach
        void setupDeletedUser() {
            // 모든 테스트 전에 논리적으로 삭제된 사용자를 미리 생성합니다.
            deletedUser = userService.createUser("deletedUser", "password", "deleted@example.com", "탈퇴한사용자", "010-0000-0000");
            deletedUserId = deletedUser.getId();
            userService.softDeleteById(deletedUserId);
        }

        @Test
        @DisplayName("논리적으로 삭제된 사용자의 프로필 수정을 시도하면 예외가 발생한다")
        void updateProfile_shouldFail_forSoftDeletedUser() {
            // when & then
            // updateProfile 내부에서 findByIdNonDel을 사용해야 올바르게 동작합니다. (현재 코드는 findById를 사용)
            // 만약 findById를 사용한다면, isDeleted 체크 로직이 updateProfile에 추가되어야 합니다.
            // 여기서는 findByIdNonDel을 사용한다는 가정 하에 테스트합니다.

            // JCFUserService의 updateProfile을 findByIdNonDel을 사용하도록 수정했다고 가정
            // User user = findById(userId); -> User user = findByIdNonDel(userId);

            // 위와 같이 수정했다면, 아래 테스트는 통과합니다.
            assertThatThrownBy(() -> userService.updateProfile(deletedUserId, "새로운닉네임", "new@email.com", "010-1234-5678"))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("데이터를 찾을 수 없거나 이미 삭제되었습니다");
        }

        @Test
        @DisplayName("논리적으로 삭제된 사용자의 상태 변경(goOnline)을 시도하면 예외가 발생한다")
        void goOnline_shouldFail_forSoftDeletedUser() {
            // when & then
            // goOnline 내부에서도 findByIdNonDel을 사용하도록 수정했다고 가정
            assertThatThrownBy(() -> userService.goOnline(deletedUserId))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("논리적으로 삭제된 사용자의 비밀번호 변경을 시도하면 예외가 발생한다")
        void changePassword_shouldFail_forSoftDeletedUser() {
            // when & then
            // changePassword 내부에서도 findByIdNonDel을 사용하도록 수정했다고 가정
            assertThatThrownBy(() -> userService.changePassword(deletedUserId, "newPassword123"))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("논리적으로 삭제된 사용자를 물리적으로 삭제(deleteById)할 수 있다")
        void deleteById_shouldWork_forSoftDeletedUser() {
            // given
            assertThat(userRepository.existsById(deletedUserId)).isTrue(); // 삭제 전에는 존재

            // when
            userService.deleteById(deletedUserId);

            // then
            assertThat(userRepository.existsById(deletedUserId)).isFalse(); // 영구 삭제 후에는 존재하지 않음
            assertThat(userRepository.count()).isZero();
        }
    }

    @Nested
    @DisplayName("사용자 재가입 및 데이터 충돌 시나리오")
    class UserReRegistrationScenarios {

        @Test
        @DisplayName("논리적으로 삭제된 사용자와 같은 username으로 재가입 시도 시 예외가 발생한다")
        void createUser_shouldFail_whenUsernameExistsAsSoftDeleted() {
            // given
            String sharedUsername = "re-register-user";
            User originalUser = userService.createUser(sharedUsername, "password", "original@email.com", null, null);
            userService.softDeleteById(originalUser.getId()); // 기존 유저 탈퇴

            // when & then
            // createUser는 existsByUsername을 사용하고, 이는 isDeleted와 상관없이 모든 데이터를 검사하므로 예외가 발생해야 함
            assertThatThrownBy(() -> userService.createUser(sharedUsername, "new_password", "new@email.com", null, null))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 존재하는 사용자 이름입니다");
        }

        @Test
        @DisplayName("논리적 삭제 후 영구 삭제된 사용자와 같은 username으로는 재가입이 가능하다")
        void createUser_shouldSucceed_afterPermanentDeletion() {
            // given
            String sharedUsername = "permanent-deleted-user";
            User originalUser = userService.createUser(sharedUsername, "password", "original@email.com", null, null);
            userService.softDeleteById(originalUser.getId()); // 1. 논리적 삭제
            userService.deleteAllByIsDel();                   // 2. 영구 삭제

            // when
            User newUser = null;
            // 예외가 발생하지 않아야 함
            assertThatCode(() -> {
                userService.createUser(sharedUsername, "new_password", "new@email.com", null, null);
            }).doesNotThrowAnyException();

            // then
            assertThat(userRepository.count()).isEqualTo(1);
            assertThat(userService.findByUsername(sharedUsername)).isNotNull();
        }
    }
}