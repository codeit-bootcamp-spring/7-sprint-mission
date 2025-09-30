import com.sprint.mission.discodeit.entity.State;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * JCFUserService의 주요 기능들에 대한 통합 테스트입니다.
 * 실제 UserRepository 인스턴스를 주입하여 서비스와 리포지토리의 상호작용을 함께 검증합니다.
 */
class JCFUserServiceIntegrationTest {

    private JCFUserService userService;
    private UserRepository userRepository;

    /**
     * 각 테스트 실행 전에 독립적인 테스트 환경을 구축합니다.
     * 의존성 주입(DI) 패턴을 테스트에도 동일하게 적용하여,
     * 실제 애플리케이션 실행 환경과 테스트 환경의 구조를 일치시킵니다.
     */
    @BeforeEach
    void setUp() {
        // 1. 테스트의 '제어권' 하에 있는 새로운 Repository 인스턴스를 생성합니다.
        //    이를 통해 테스트는 데이터 저장소의 상태를 완벽하게 격리하고 제어할 수 있습니다.
        this.userRepository = new UserRepository();
        // 2. 생성한 Repository를 Service에 '주입'하여 테스트 대상을 설정합니다.
        //    이로써 Service는 우리가 제어하는 데이터 저장소를 바라보게 됩니다.
        this.userService = new JCFUserService(this.userRepository);
    }

    @Test
    @DisplayName("사용자 생성 성공 시나리오")
    void userCreation_Success_Scenario() {
        // Arrange (준비): 사용자 생성을 위한 데이터를 정의합니다.
        String username = "gemini";
        String email = "gemini@google.com";
        String nickname = "AI";

        // Act (실행): Service의 핵심 비즈니스 로직을 호출합니다.
        User createdUser = userService.createUser(username, "password123", email, nickname, null);

        // Assert (검증): Service가 반환한 결과와 시스템의 최종 상태를 모두 검증합니다.
        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(createdUser.isOnline()).isTrue();

        // [통합 테스트 핵심] Service의 로직이 Repository에 올바르게 반영되었는지,
        // 데이터의 최종 상태를 직접 확인하여 검증의 신뢰도를 높입니다.
        User foundUserInRepo = userRepository.findById(createdUser.getId()).get();
        assertThat(foundUserInRepo).isEqualTo(createdUser);
    }

    // ... (다른 테스트 메서드들도 필요에 따라 상세한 주석 추가) ...

    @Test
    @DisplayName("사용자 프로필 수정 시나리오")
    void userProfileUpdate_Success_Scenario() {
        // Arrange (준비)
        User user = userService.createUser("gemini", "pw", "email@a.com", "Old Nickname", "010-0000-0000");
        Long initialUpdatedAt = user.getUpdatedAt();

        // Act (실행): 프로필 수정을 요청합니다.
        String newNickname = "New Gemini";
        User updatedUser = userService.updateProfile(user.getId(), newNickname, "new@google.com", null);

        // Assert (검증)
        User finalUser = userService.findById(user.getId()); // 저장소에서 최종 상태를 다시 명확하게 조회

// 1. 닉네임이 'New Gemini'로 "정확히" 변경되었는가?
        assertThat(finalUser.getNickname()).isEqualTo("New Gemini");

// 2. 이메일이 'new@google.com'으로 "정확히" 변경되었는가?
        assertThat(finalUser.getEmail()).isEqualTo("new@google.com");

// 3. 변경하지 않은 phoneNum은 "그대로" 유지되었는가?
        assertThat(finalUser.getPhoneNum()).isEqualTo("010-0000-0000");
        // 수정이 일어났으므로, updatedAt 타임스탬프는 초기값보다 커야 합니다.
        assertThat(updatedUser.getUpdatedAt()).isGreaterThan(initialUpdatedAt);
    }
}