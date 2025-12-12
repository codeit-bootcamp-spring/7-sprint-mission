package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.domain.channel.ChannelNotExistException;
import com.sprint.mission.discodeit.exception.domain.readStatus.ReadStatusNotExistException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
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
    private ReadStatus readStatus2;
    @BeforeEach
    void setUp() {
        user = User.createUserFactory("user1","111@user","1234");
        channelPrivate = Channel.privateChannelFactory(
                "privateChannel",
                "privateChannelDesc"
        );
        channelPublic = Channel.publicChannelFactory(
                "publicChannel",
                "publicChannelDesc"
        );
        readStatus1 = ReadStatus.createReadStatusFactory(
                user,channelPrivate
        );
        readStatus2 = ReadStatus.createReadStatusFactory(
                user,channelPublic
        ) ;

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

        then(channelRepository).should(times(1)).save(any(Channel.class));
        then(userRepository).should(times(1)).findById(any(UUID.class));
        then(readStatusRepository).should(times(1)).save(any(ReadStatus.class));
        then(channelMapper).should(times(1)).toDto(any(Channel.class));

    }

    @Test
    @DisplayName("[예외 케이스] 사설 채널 생성 실패")
    void createPrivateChannel_Fail() {
        given(userRepository.findById(any(UUID.class))).willThrow(new UserNotExistException(UUID.randomUUID()));

        assertThatThrownBy(()-> channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto(
                new HashSet<>(List.of(UUID.randomUUID()))
        ))).isInstanceOf(UserNotExistException.class);

        then(channelRepository).should(never()).save(any(Channel.class));
    }

    @Test
    @DisplayName("[정상 케이스 ] 공용 채널 생성 성공")
    void createPublicChannel_Success() {
        given(channelRepository.save(any(Channel.class))).willReturn(channelPublic);
        given(userRepository.findAll()).willReturn(List.of(user));
        given(readStatusRepository.save(any(ReadStatus.class))).willReturn(readStatus2);
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        ChannelDto response = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());

        assertThat(response).isEqualTo(channelDto);

        then(channelRepository).should(times(1)).save(any(Channel.class));
        then(userRepository).should(times(1)).findAll();
        then(readStatusRepository).should(times(1)).save(any(ReadStatus.class));
        then(channelMapper).should(times(1)).toDto(any(Channel.class));

    }

    @Test
    @DisplayName("[예외 케이스] 공용 채널 생성 실패")
    void createPublicChannel_Fail() {
        given(userRepository.findAll()).
                willThrow(new UserNotExistException(UUID.randomUUID()));

        assertThatThrownBy(()-> channelService.createPublicChannel(TestFixture.channelPublicCreateFactory()))
                .isInstanceOf(UserNotExistException.class);

        then(channelRepository).should(never()).save(any(Channel.class));
        then(readStatusRepository).should(never()).save(any(ReadStatus.class));
    }

    @Test
    @DisplayName("[정상 케이스] 채널 변경 성공")
    void patchChannel_Success() {

        given(channelRepository.findById(any(UUID.class))).willReturn(Optional.of(channelPrivate));
        given(channelRepository.save(any(Channel.class))).willReturn(channelPrivate);
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        ChannelDto response = channelService.patchChannel(TestFixture.channelPatchFactory(), UUID.randomUUID());

        assertThat(response).isEqualTo(channelDto);

        then(channelRepository).should(times(1)).findById(any(UUID.class));
        then(channelRepository).should(times(1)).save(any(Channel.class));
        then(channelMapper).should(times(1)).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("[예외 케이스] 채널 변경 실패")
    void patchChannel_Fail() {
        given(channelRepository.findById(any(UUID.class)))
                .willThrow(new ChannelNotExistException(UUID.randomUUID()));

        assertThatThrownBy(()-> channelService.patchChannel(TestFixture.channelPatchFactory(), UUID.randomUUID()))
                .isInstanceOf(ChannelNotExistException.class);

        then(channelRepository).should(never()).save(any(Channel.class));


    }

    @Test
    @DisplayName("[정상 케이스] 채널 삭제 성공")
    void deleteChannel_Success() {
        given(channelRepository.existsById(any(UUID.class))).willReturn(true);
        willDoNothing().given(channelRepository).deleteById(any(UUID.class));

        channelService.deleteChannel(UUID.randomUUID());
        then(channelRepository).should(times(1)).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("[예외 케이스] 채널 삭제 실패")
    void deleteChannel_Fail() {

       assertThatThrownBy(()-> channelService.deleteChannel(UUID.randomUUID()))
               .isInstanceOf(ChannelNotExistException.class);

       then(channelRepository).should(never()).deleteById(any(UUID.class));


    }

    @Test
    @DisplayName("[정상 케이스] 유저 id로 조회")
    void findAllByUserId_Success() {
        given(channelRepository.findAll()).willReturn(List.of(channelPrivate,channelPublic));
        given(readStatusRepository.findAll()).willReturn(List.of());
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        List<ChannelDto> response = channelService.findAllByUserId(UUID.randomUUID());

        assertThat(response).containsExactly(channelDto);

        then(channelRepository).should(times(1)).findAll();
        then(readStatusRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("[예외 케이스] 유저 id로 조회 실패")
    void findAllByUserId_Fail() {
        given(channelRepository.findAll()).willReturn(List.of(channelPrivate, channelPublic));
        given(readStatusRepository.findAll())
                .willThrow(new ReadStatusNotExistException(UUID.randomUUID()));

        assertThatThrownBy(() -> channelService.findAllByUserId(UUID.randomUUID()))
                .isInstanceOf(ReadStatusNotExistException.class);

        then(channelMapper).should(never()).toDto(any(Channel.class));
    }
}
