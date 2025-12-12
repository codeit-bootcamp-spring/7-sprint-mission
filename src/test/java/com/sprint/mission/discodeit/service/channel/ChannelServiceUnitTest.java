package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChannelService Unit Test")
public class ChannelServiceUnitTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ChannelMapper channelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @InjectMocks
    private BasicChannelService channelService;

    private Channel channelPrivate;
    private Channel channelPublic;
    private ReadStatus readStatus1;
    private User user;
    private ChannelDto channelDto;
    @BeforeEach
    void setUp() {
        user = User.createUserFactory("user1","111@user","1234");
        channelPrivate = Channel.privateChannelFactory(
                "privateChannel",
                "privateChannelDesc"
        );
        readStatus1 = ReadStatus.createReadStatusFactory(
                user,channelPrivate
        );

    }

    @Test
    @DisplayName("[정상 케이스] 사설 채널 생성")
    void createPrivateChannel_Success() {
        given(channelRepository.save(any(Channel.class))).willReturn(channelPrivate);
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));
        given(readStatusRepository.save(any(ReadStatus.class))).willReturn(readStatus1);
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        ChannelDto response = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto(
                new HashSet<>(List.of(UUID.randomUUID()))
        ));

        assertThat(response).isEqualTo(channelDto);

        verify(channelRepository,times(1)).save(any(Channel.class));
        verify(userRepository,times(1)).findById(any(UUID.class));
        verify(readStatusRepository,times(1)).save(any(ReadStatus.class));
        verify(channelMapper,times(1)).toDto(any(Channel.class));

    }

    @Test
    @DisplayName("[예외 케이스] 사설 채널 생성 실패")
    void createPrivateChannel_Fail() {

    }

    @Test
    @DisplayName("[정상 케이스 ] 공용 채널 생성 성공")
    void createPublicChannel_Success() {

    }

    @Test
    @DisplayName("[예외 케이스] 공용 채널 생성 실패")
    void createPublicChannel_Fail() {

    }

    @Test
    @DisplayName("[정상 케이스] 채널 변경 성공")
    void patchChannel_Success() {}

    @Test
    @DisplayName("[예외 케이스] 채널 변경 실패")
    void patchChannel_Fail() {}

    @Test
    @DisplayName("[정상 케이스] 채널 삭제 성공")
    void deleteChannel_Success() {}

    @Test
    @DisplayName("[예외 케이스] 채널 삭제 실패")
    void deleteChannel_Fail() {

    }
}
