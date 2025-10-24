package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.impl.ChannelRepositoryImpl;
import com.sprint.mission.discodeit.repository.impl.ParticipationRepositoryImpl;
import com.sprint.mission.discodeit.repository.impl.UserRepositoryImpl;
import com.sprint.mission.discodeit.service.impl.ParticipationServiceImpl;
import com.sprint.mission.discodeit.utils.ParticipationDualKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ParticipationServiceImpl 통합 테스트")
class JCFParticipationServiceTest {

    private ParticipationServiceImpl participationService;
    private UserRepositoryImpl userRepository;
    private ChannelRepositoryImpl channelRepository;
    private ParticipationRepositoryImpl participationRepository;

    // 테스트에 사용할 기본 사용자 및 채널
    private User adminUser, normalUser, anotherUser;
    private Channel publicChannel;

    @BeforeEach
    void setUp() {
        // 매 테스트마다 깨끗한 상태에서 시작하도록 저장소와 서비스를 새로 생성
        userRepository = new UserRepositoryImpl();
        channelRepository = new ChannelRepositoryImpl();
        participationRepository = new ParticipationRepositoryImpl();

        participationService = new ParticipationServiceImpl(participationRepository, userRepository, channelRepository);

        // 테스트에 공통적으로 사용할 사용자 및 채널 데이터 생성
        adminUser = User.create("admin", "pw", "admin@a.com", "관리자", null);
        normalUser = User.create("user", "pw", "user@a.com", "일반사용자", null);
        anotherUser = User.create("another", "pw", "another@a.com", "다른사용자", null);
        publicChannel = Channel.create("공용 채널", null, "누구나", false);

        // 저장소에 저장
        userRepository.save(adminUser);
        userRepository.save(normalUser);
        userRepository.save(anotherUser);
        channelRepository.save(publicChannel);
    }

    @Nested
    @DisplayName("채널 참여 (joinChannel) 테스트")
    class JoinChannelTests {

        @Test
        @DisplayName("성공: 사용자가 채널에 처음으로 참여한다")
        void joinChannel_success_forFirstTime() {
            // when
            Participation participation = participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "닉네임1");

            // then
            assertThat(participation).isNotNull();
            assertThat(participation.getUserId()).isEqualTo(normalUser.getId());
            assertThat(participation.getChannelId()).isEqualTo(publicChannel.getId());
            assertThat(participation.getNickname()).isEqualTo("닉네임1");
            assertThat(participation.getRole()).isEqualTo(Role.USER);
            assertThat(participationService.isUserInChannel(publicChannel.getId(), normalUser.getId())).isTrue();
        }

        @Test
        @DisplayName("실패: 이미 참여 중인 채널에 다시 참여를 시도하면 IllegalStateException 예외가 발생한다")
        void joinChannel_fail_whenAlreadyJoined() {
            // given
            participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "닉네임1");

            // when & then
            assertThatThrownBy(() -> participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "다른닉네임"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 채널에 참가 되어있습니다.");
        }

        @Test
        @DisplayName("실패: 존재하지 않는 사용자로 참여 시도 시 NoSuchElementException 예외가 발생한다")
        void joinChannel_fail_withNonExistentUser() {
            // when & then
            assertThatThrownBy(() -> participationService.joinChannel(publicChannel.getId(), UUID.randomUUID(), "닉네임"))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("존재하지 않거나 탈퇴한 사용자입니다.");
        }
    }

    @Nested
    @DisplayName("채널 나가기 (leaveChannel) 및 복합 시나리오")
    class LeaveAndRejoinChannelTests {

        @Test
        @DisplayName("성공: 마지막 참여자가 채널에서 나가면 true를 반환한다")
        void leaveChannel_success_asLastUser() {
            // given
            participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "혼자");

            // when
            boolean isChannelEmpty = participationService.leaveChannel(publicChannel.getId(), normalUser.getId());

            // then
            assertThat(isChannelEmpty).isTrue();
            assertThat(participationService.isUserInChannel(publicChannel.getId(), normalUser.getId())).isFalse();
        }

        @Test
        @DisplayName("실패: 채널의 마지막 관리자는 채널을 떠날 수 없다")
        void leaveChannel_fail_asLastAdmin() {
            // given
            participationService.joinChannel(publicChannel.getId(), adminUser.getId(), "관리자");
            // 자기 자신을 ADMIN으로 만드는 것은 불가능하므로, 다른 관리자가 역할을 변경해줘야 함
            // 테스트를 위해 임시 관리자를 만들어서 역할을 변경
            User tempAdmin = User.create("tempAdmin", "p", "t@a.com", null, null);
            userRepository.save(tempAdmin);


            participationService.joinChannel(publicChannel.getId(), tempAdmin.getId(), "유저005");// 스스로 ADMIN이 됨
            participationService.changeRole(publicChannel.getId(), adminUser.getId(), tempAdmin.getId(), Role.ADMIN);

            // when & then
            assertThatThrownBy(() -> participationService.leaveChannel(publicChannel.getId(), adminUser.getId()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("채널의 마지막 관리자는 채널을 떠날 수 없습니다.");
        }

        @Test
        @DisplayName("복합 시나리오: 나갔던 채널에 다시 참여하면 기존 참여 정보가 복원(restore)된다")
        void joinChannel_complex_rejoinAfterLeave() {
            // given: 1. 채널 참여
            Participation firstParticipation = participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "첫번째닉네임");
            long originalCreatedAt = firstParticipation.getCreatedAt();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 2. 채널 나가기 (논리적 삭제)
            participationService.leaveChannel(publicChannel.getId(), normalUser.getId());
            assertThat(participationService.isUserInChannel(publicChannel.getId(), normalUser.getId())).isFalse();

            // when: 3. 다른 닉네임으로 다시 참여
            Participation rejoinParticipation = participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "두번째닉네임");

            // then
            assertThat(rejoinParticipation.getId()).isEqualTo(firstParticipation.getId());
            assertThat(rejoinParticipation.isDeleted()).isFalse();
            assertThat(rejoinParticipation.getNickname()).isEqualTo("두번째닉네임");
            assertThat(rejoinParticipation.getCreatedAt()).isEqualTo(originalCreatedAt);
            assertThat(rejoinParticipation.getUpdatedAt()).isGreaterThan(originalCreatedAt);
        }
    }

    @Nested
    @DisplayName("관리자 기능 (kick, role, owner) 테스트")
    class AdminFunctionTests {

        @BeforeEach
        void setupParticipants() {
            // 관리자와 일반 사용자가 채널에 미리 참여
            participationService.joinChannel(publicChannel.getId(), adminUser.getId(), "관리자닉");
            // 테스트에서는 ADMIN 역할을 직접 부여해줘야 함
            Participation adminParticipation = participationRepository.findById(new ParticipationDualKey(publicChannel.getId(), adminUser.getId())).get();
            adminParticipation.changeRole(Role.ADMIN);
            participationRepository.save(adminParticipation);

            participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "일반닉");
        }

        @Test
        @DisplayName("성공: 관리자가 일반 사용자를 강제 퇴장시킨다")
        void kickUser_success_byAdmin() {
            // when
            participationService.kickUserFromChannel(publicChannel.getId(), normalUser.getId(), adminUser.getId());

            // then
            assertThat(participationService.isUserInChannel(publicChannel.getId(), normalUser.getId())).isFalse();
        }

        @Test
        @DisplayName("실패: 관리자가 자기 자신을 강퇴 시도 시 IllegalArgumentException 예외가 발생한다")
        void kickUser_fail_onSelfKick() {
            // when & then
            assertThatThrownBy(() -> participationService.kickUserFromChannel(publicChannel.getId(), adminUser.getId(), adminUser.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("자기 자신을 강퇴할 수 없습니다.");
        }


        @Test
        @DisplayName("성공: findOwner는 채널의 관리자(ADMIN)를 정확히 찾아낸다")
        void findOwner_success() {
            // when
            User owner = participationService.findOwner(publicChannel.getId());

            // then
            assertThat(owner.getId()).isEqualTo(adminUser.getId());
        }

        @Test
        @DisplayName("성공: 관리자가 일반 사용자의 역할을 GUEST로 변경한다")
        void changeRole_success_byAdmin() {
            // when
            participationService.changeRole(publicChannel.getId(), normalUser.getId(), adminUser.getId(), Role.GUEST);

            // then
            Participation updatedParticipation = participationService.findById(new ParticipationDualKey(publicChannel.getId(), normalUser.getId()));
            assertThat(updatedParticipation.getRole()).isEqualTo(Role.GUEST);
        }

        @Test
        @DisplayName("실패: 일반 사용자가 다른 사용자의 역할을 변경 시도 시 SecurityException 예외가 발생한다")
        void changeRole_fail_byNormalUser() {
            // when & then
            assertThatThrownBy(() -> participationService.changeRole(publicChannel.getId(), adminUser.getId(), normalUser.getId(), Role.USER))
                    .isInstanceOf(SecurityException.class)
                    .hasMessage("사용자의 역할을 변경할 권한이 없습니다.");
        }

        @Test
        @DisplayName("실패: 마지막 관리자의 역할을 USER로 변경 시도 시 IllegalStateException 예외가 발생한다")
        void changeRole_fail_onLastAdmin() {
            // when & then
            assertThatThrownBy(() -> participationService.changeRole(publicChannel.getId(), adminUser.getId(), adminUser.getId(), Role.USER))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("채널의 마지막 관리자 역할은 변경할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("다양한 조건 (Parameterized) 테스트")
    class MultiConditionTests {

        @ParameterizedTest
        @CsvSource({
                "ADMIN, true",
                "USER, false",
                "GUEST, false"
        })
        @DisplayName("isOwner: 다양한 역할(Role)에 따라 소유주 여부를 정확히 반환한다")
        void isOwner_shouldReturnCorrectResult_forEachRole(Role role, boolean expectedResult) {
            // given
            participationService.joinChannel(publicChannel.getId(), normalUser.getId(), "테스트유저");
            Participation p = participationRepository.findById(new ParticipationDualKey(publicChannel.getId(), normalUser.getId())).get();
            p.changeRole(role); // 직접 역할 변경
            participationRepository.save(p);

            // when
            boolean isOwner = participationService.isOwner(publicChannel.getId(), normalUser.getId());

            // then
            assertThat(isOwner).isEqualTo(expectedResult);
        }
    }
}