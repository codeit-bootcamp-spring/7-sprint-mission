import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class JCFChannelServiceTest {

    private JCFChannelService channelService;
    private JCFChannelRepository channelRepository; // 의존성 추가

    @BeforeEach
    void setUp() {
        // 각 테스트는 독립적으로 실행되어야 하므로, 매번 새로운 Repository와 Service 객체를 생성합니다.
        channelRepository = new JCFChannelRepository(); // 추가
        channelService = new JCFChannelService(channelRepository);
    }

    @Test
    @DisplayName("새로운 채널을 성공적으로 생성한다")
    void create_should_createChannelSuccessfully() {
        // given (준비)
        String channelName = "일반";
        ChannelType type = ChannelType.CHAT;

        // when (실행)
        Channel createdChannel = channelService.create(channelName, type, "자유롭게 대화하세요", false);

        // then (검증)
        assertThat(createdChannel).isNotNull();
        assertThat(createdChannel.getChannelName()).isEqualTo(channelName);
        assertThat(channelRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 채널 생성 시 예외가 발생한다")
    void create_should_throwException_when_nameIsDuplicate() {
        // given
        channelService.create("중복된-이름", ChannelType.CHAT, null, false);

        // when & then
        assertThatThrownBy(() -> {
            channelService.create("중복된-이름", ChannelType.CHAT, "다른 토픽", true);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 채널 이름입니다");
    }

    @Test
    @DisplayName("채널 설정을 성공적으로 변경한다")
    void changeSettings_should_updateChannelProperties() throws InterruptedException {
        // given
        Channel channel = channelService.create("옛날 이름", ChannelType.VIDEO, "옛날 토픽", true);
        UUID channelId = channel.getId();
        long initialUpdatedAt = channel.getUpdatedAt();
        Thread.sleep(1000);//처리과정이 거의 동시에 일어나 UpdateAt이 바뀌지는 안는 문제점 확인 통과 완료
        // when
        channelService.changeSettings(channelId, "새로운 이름", ChannelType.CHAT, "새로운 토픽", false);

        // then
        Channel updatedChannel = channelService.findById(channelId);
        assertThat(updatedChannel.getChannelName()).isEqualTo("새로운 이름");
        assertThat(updatedChannel.getChannelType()).isEqualTo(ChannelType.CHAT);
        assertThat(updatedChannel.getTopic()).isEqualTo("새로운 토픽");
        assertThat(updatedChannel.isPrivate()).isFalse();
        assertThat(updatedChannel.getUpdatedAt()).isGreaterThan(initialUpdatedAt);
    }

    @Test
    @DisplayName("채널 이름 변경 시 다른 채널의 이름과 중복되면 예외가 발생한다")
    void changeSettings_should_throwException_when_newNameIsDuplicate() {
        // given
        channelService.create("이미-있는-이름", ChannelType.CHAT, null, false);
        Channel channelToUpdate = channelService.create("바꿀-채널", ChannelType.CHAT, null, false);

        // when & then
        assertThatThrownBy(() -> {
            channelService.changeSettings(channelToUpdate.getId(), "이미-있는-이름", null, null, null);
        })
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("채널 이름으로 채널을 찾을 수 있다")
    void findByChannelName_should_returnChannel() {
        // given
        String targetName = "찾을 채널";
        channelService.create(targetName, ChannelType.CHAT, null, false);

        // when
        Channel foundChannel = channelService.findByChannelName(targetName);

        // then
        assertThat(foundChannel).isNotNull();
        assertThat(foundChannel.getChannelName()).isEqualTo(targetName);
    }

    @Test
    @DisplayName("존재하지 않는 채널 이름으로 조회 시 예외가 발생한다")
    void findByChannelName_should_throwException_when_channelNotFound() {
        // when & then
        assertThatThrownBy(() -> {
            channelService.findByChannelName("없는-채널");
        })
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("여러 조건으로 채널 목록을 정확히 필터링한다")
    void findAllBySettings_should_filterChannelsCorrectly() {
        // given
        channelService.create("일반", ChannelType.CHAT, "자바", false);
        channelService.create("개발", ChannelType.CHAT, "코틀린", false);
        channelService.create("안녕", ChannelType.VIDEO, "인사", true);
        channelService.create("질문", ChannelType.CHAT, "자바", true);

        // when
        // [수정된 부분] 조건: 타입은 CHAT이고, 토픽에 "자바"가 포함되어야 함
        List<Channel> foundChannels = channelService.findAllChannelsBySettings(null, ChannelType.CHAT, "자바");

        // then
        assertThat(foundChannels).hasSize(2);
        assertThat(foundChannels)
                .extracting(Channel::getChannelName) // 채널 이름만 추출하여
                .containsExactlyInAnyOrder("일반", "질문"); // 해당 이름들이 있는지 확인
    }

    @Test
    @DisplayName("검색 조건에 맞는 채널이 없으면 빈 리스트를 반환한다")
    void findAllBySettings_should_returnEmptyList_when_noMatch() {
        // given
        channelService.create("일반", ChannelType.CHAT, "자바", false);

        // when
        // 조건: 타입은 DIRECT (테스트 데이터에 없음)
        List<Channel> foundChannels = channelService.findAllChannelsBySettings(null, ChannelType.DIRECT, null);

        // then
        assertThat(foundChannels).isNotNull().isEmpty();
    }
}