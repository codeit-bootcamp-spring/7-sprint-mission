package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFDirectMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFParticipationRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JCFUserService 심층 기능 테스트")
class JCFUserServiceAdvancedTest {

    private JCFUserService userService;
    private JCFUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userService = new JCFUserService(userRepository, new JCFParticipationRepository(), new JCFChannelMessageRepository(), new JCFDirectMessageRepository());
    }

    @Nested
    @DisplayName("User 엔티티 내부 상태 변경 규칙 검증")
    class UserStateLogicTests {

        @Test
        @DisplayName("오프라인 상태의 사용자를 '자리 비움'으로 변경 시도 시 IllegalStateException 예외가 발생한다")
        void setAway_shouldFail_whenUserIsOffline() {
            // given
            User user = userService.createUser("testUser", "p", "test@test.com", null, null);
            userService.goOffline(user.getId()); // 사용자를 오프라인 상태로 만듦
            assertThat(user.isOffline()).isTrue();

            // when & then
            assertThatThrownBy(() -> userService.setAway(user.getId()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("오프라인 상태에서는 자리 비움으로 변경할 수 없습니다.");
        }

        @Test
        @DisplayName("프로필 수정 시 실제 변경된 내용이 없으면 updatedAt 타임스탬프는 변경되지 않는다")
        void updateProfile_shouldNotUpdateTimestamp_whenDataIsUnchanged() throws InterruptedException {
            // given
            User user = userService.createUser("user", "p", "user@a.com", "MyNick", "123");
            long initialUpdatedAt = user.getUpdatedAt();
            Thread.sleep(10); // 시간차를 확실하게 주기 위함

            // when
            // 기존과 동일한 정보로 프로필 업데이트 시도
            userService.updateProfile(user.getId(), "MyNick", "user@a.com", "123");

            // then
            User updatedUser = userService.findById(user.getId());
            assertThat(updatedUser.getUpdatedAt()).isEqualTo(initialUpdatedAt);
        }

        @Test
        @DisplayName("8자 미만의 비밀번호로 변경 시도 시 IllegalArgumentException 예외가 발생한다")
        void changePassword_shouldFail_whenPasswordIsTooShort() {
            // given
            User user = userService.createUser("user", "p", "user@a.com", null, null);

            // when & then
            assertThatThrownBy(() -> userService.changePassword(user.getId(), "1234567"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("비밀번호는 8자 이상이어야 합니다.");
        }
    }

    @Nested
    @DisplayName("벌크(Bulk) 데이터 처리 시나리오 검증")
    class BulkOperationTests {

        private User activeUser;
        private User softDeletedUser;
        private UUID nonExistentId;

        @BeforeEach
        void setupUsers() {
            activeUser = userService.createUser("activeUser", "p", "active@a.com", null, null);
            softDeletedUser = userService.createUser("deletedUser", "p", "deleted@a.com", null, null);
            userService.softDeleteById(softDeletedUser.getId());
            nonExistentId = UUID.randomUUID();
        }

        @Test
        @DisplayName("findAllById: 여러 상태의 ID 목록으로 조회 시, 존재하는 모든 사용자를 반환한다")
        void findAllById_shouldReturnAllExistingUsersRegardlessOfDeletionStatus() {
            // given
            List<UUID> idsToFind = List.of(activeUser.getId(), softDeletedUser.getId(), nonExistentId);

            // when
            List<User> foundUsers = userService.findAllById(idsToFind);

            // then
            assertThat(foundUsers).hasSize(2);
            assertThat(foundUsers)
                    .extracting(User::getUsername)
                    .containsExactlyInAnyOrder("activeUser", "deletedUser");
        }

        @Test
        @DisplayName("findAllByIdNonDel: 여러 상태의 ID 목록으로 조회 시, 활성 상태의 사용자만 반환한다")
        void findAllByIdNonDel_shouldReturnOnlyActiveUsers() {
            // given
            List<UUID> idsToFind = List.of(activeUser.getId(), softDeletedUser.getId(), nonExistentId);

            // when
            List<User> foundUsers = userService.findAllByIdNonDel(idsToFind);

            // then
            assertThat(foundUsers).hasSize(1);
            assertThat(foundUsers.get(0).getUsername()).isEqualTo("activeUser");
        }
    }
}