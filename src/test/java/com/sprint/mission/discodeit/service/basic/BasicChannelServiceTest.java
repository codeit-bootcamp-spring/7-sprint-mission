package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {
    @InjectMocks
    BasicChannelService channelService;

    @Mock
    ChannelRepository channelRepository;

    @Mock
    ReadStatusRepository readStatusRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    ChannelMapper channelMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    UserStatusRepository userStatusRepository;

    @Test
    @DisplayName("createPublic 성공: 채널 저장 후 매핑")
    void createPublic_success() {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelCreateRequestDto publicChannelCreateRequestDto
                = new PublicChannelCreateRequestDto(
                        "testChannel",
                "description",
                0,
                ChannelType.PUBLIC);
        given(channelRepository.save(any(Channel.class))).willAnswer(invo -> {
            Channel channel = invo.getArgument(0);
            setId(channel, channelId);
            setCreatedAt(channel, Instant.now());
            return channel;
        });

        given(readStatusRepository.findAllByChannelId(channelId)).willReturn(List.of());
        given(messageRepository.findByChannelId(channelId)).willReturn(List.of());

        ChannelResponseDto mockResponse = mock(ChannelResponseDto.class);
        given(channelMapper.toDto(any(Channel.class), any(), anyList(), anyMap())).willReturn(mockResponse);

        // when
        ChannelResponseDto created = channelService.createPublic(publicChannelCreateRequestDto);

        // then
        assertThat(created).isSameAs(mockResponse);
        then(channelRepository).should().save(any(Channel.class));
        then(readStatusRepository).should().findAllByChannelId(channelId);
        then(channelMapper).should().toDto(any(Channel.class), any(), anyList(), anyMap());

    }

    @Test
    @DisplayName("createPrivate 성공: 채널 저장 후 매핑")
    void createPrivate_success() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID u1 = UUID.randomUUID();
        UUID u2 = UUID.randomUUID();

        PrivateChannelCreateRequestDto privateChannelCreateRequestDto
                = new PrivateChannelCreateRequestDto(List.of(u1, u2), 0, ChannelType.PRIVATE);
        given(channelRepository.save(any(Channel.class))).willAnswer(invo -> {
            Channel channel = invo.getArgument(0);
            setId(channel, channelId);
            setCreatedAt(channel, Instant.now());
            return channel;
        });

        User user1 = new User("user1", "aaaaa111111", "user1@naver.com", null);
        User user2 = new User("user2", "dsafdsfsadfa111", "user2@naver.com", null);
        setId(user1, u1);
        setId(user2, u2);

        given(userRepository.findById(u1)).willReturn(Optional.of(user1));
        given(userRepository.findById(u2)).willReturn(Optional.of(user2));

        Channel channel = new Channel(ChannelType.PRIVATE, null, true, 0, null);
        setId(channel, channelId);
        setCreatedAt(channel, Instant.now());

        ReadStatus rs1 = new ReadStatus(user1, channel, channel.getCreatedAt());
        ReadStatus rs2 = new ReadStatus(user2, channel, channel.getCreatedAt());

        given(readStatusRepository.findAllByChannelId(channelId)).willReturn(List.of(rs1, rs2));

        given(userStatusRepository.findAllByUserIdIn(anySet())).willReturn(List.of());
        given(messageRepository.findByChannelId(channelId)).willReturn(List.of());

        ChannelResponseDto mockResponse = mock(ChannelResponseDto.class);
        given(channelMapper.toDto(any(Channel.class), any(), anyList(), anyMap())).willReturn(mockResponse);

        // when
        ChannelResponseDto created = channelService.createPrivate(privateChannelCreateRequestDto);

        // then
        assertThat(created).isSameAs(mockResponse);
        then(userRepository).should().findById(u1);
        then(userRepository).should().findById(u2);
        then(readStatusRepository).should(atLeast(2)).save(any(ReadStatus.class));
        then(channelRepository).should().save(any(Channel.class));
        then(channelMapper).should().toDto(any(Channel.class), any(), anyList(), anyMap());
    }

    @Test
    @DisplayName("createPrivate 실패: participant 유저가 없으면 exception 발생")
    void createPrivate_fail_userNotFound() {
        // given
        UUID notUser = UUID.randomUUID();

        PrivateChannelCreateRequestDto privateChannelCreateRequestDto
                = new PrivateChannelCreateRequestDto(List.of(notUser), 0, ChannelType.PRIVATE);

        given(channelRepository.save(any(Channel.class))).willAnswer(invo -> {
            Channel channel = invo.getArgument(0);
            setId(channel, UUID.randomUUID());
            setCreatedAt(channel, Instant.now());
            return channel;
        });

        given(userRepository.findById(notUser)).willReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> channelService.createPrivate(privateChannelCreateRequestDto))
                .isInstanceOf(UserNotFoundException.class);
        then(readStatusRepository).should(never()).save(any());


    }

    @Test
    @DisplayName("update 성공: public 채널일 때 field 변경 후 저장")
    void update_success_publicChannel() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel(
                ChannelType.PUBLIC,
                "oldName",
                false,
                0,
                "oldDescription");
        setId(channel, channelId);

        ChannelUpdateRequestDto channelUpdateRequestDto
                = new ChannelUpdateRequestDto(
                        "newName", "newDescription", 3);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(channelRepository.save(any(Channel.class))).willAnswer(invo -> invo.getArgument(0));

        given(messageRepository.findByChannelId(channelId)).willReturn(List.of());
        given(readStatusRepository.findAllByChannelId(channelId)).willReturn(List.of());

        ChannelResponseDto mockResponse = mock(ChannelResponseDto.class);
        given(channelMapper.toDto(any(Channel.class), any(), anyList(), anyMap())).willReturn(mockResponse);

        // when
        ChannelResponseDto updated = channelService.update(channelId, channelUpdateRequestDto);

        // then
        assertThat(updated).isSameAs(mockResponse);
        assertThat(channel.getName()).isEqualTo("newName");
        assertThat(channel.getDescription()).isEqualTo("newDescription");
        then(channelRepository).should().save(channel);

    }

    @Test
    @DisplayName("update 실패: private 채널일 때 exception 발생")
    void update_fail_privateChannel() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel =
                new Channel(ChannelType.PRIVATE,
                        null,
                        true,
                        0,
                        null);
        setId(channel, channelId);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        ChannelUpdateRequestDto channelUpdateRequestDto
                = new ChannelUpdateRequestDto("newName", "newDescription", 2);

        // when & then
        assertThatThrownBy(() -> channelService.update(channelId, channelUpdateRequestDto))
                .isInstanceOf(PrivateChannelUpdateException.class);
        then(channelRepository).should(never()).save(any());

    }
/*
 fail 원인 : delete의 반환이 void라 Mockito의 기본 boolean 값인 false로 체크됨.
 그래서 delete 로직상 channel id 존재 여부 확인시 id가 없다면 NOT FOUND로 되어 fail 됨.
 channel id에 대해 existsById를 true로 설정하여 채널이 있다는 것을 전제로 하도록하여 해결.
 */
    @Test
    @DisplayName("delete 성공: 채널 삭제되면 메세지와 상태도 같이 삭제되어야 한다.")
    void delete_success() {
        // given
        UUID channelId = UUID.randomUUID();

        given(channelRepository.existsById(channelId)).willReturn(true);

        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);

        given(messageRepository.findByChannelId(channelId)).willReturn(List.of(message1, message2));

        // when
        channelService.delete(channelId);

        // then
        then(channelRepository).should().existsById(channelId);
        then(messageRepository).should().findByChannelId(channelId);
        then(messageRepository).should().deleteAll(List.of(message1,message2));
        then(readStatusRepository).should().deleteAllByChannelId(channelId);
        then(channelRepository).should().deleteById(channelId);


    }

    @Test
    @DisplayName("delete 실패: 채널이 없다면 exception 발생")
    void delete_fail() {
        // given
        UUID channelId = UUID.randomUUID();
        given(channelRepository.existsById(channelId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> channelService.delete(channelId))
                .isInstanceOf(ChannelNotFoundException.class);
        then(channelRepository).should().existsById(channelId);
        then(messageRepository).shouldHaveNoInteractions();
        then(readStatusRepository).shouldHaveNoInteractions();
        then(channelRepository).should(never()).deleteById(any());

    }
    
    @Test
    @DisplayName("getAllByUserId 성공: public + 이미 존재하는 private만 조회")
    void getAllByUserId_success() {
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        UUID privateChannel1Id = UUID.randomUUID();
        UUID privateChannel2Id = UUID.randomUUID();

        Channel channel = new Channel(ChannelType.PUBLIC,
                "public",
                false,
                0,
                null);
        setId(channel, channelId);

        Channel privateChannel1 = new Channel(
                ChannelType.PRIVATE,
                null,
                true,
                0,
                null);
        setId(privateChannel1, privateChannel1Id);

        Channel privateChannel2 = new Channel(
                ChannelType.PRIVATE,
                null,
                true,
                0,
                null);
        setId(privateChannel2,privateChannel2Id);

        ReadStatus rs = mock(ReadStatus.class);
        given(rs.getChannel()).willReturn(privateChannel1);
        given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(rs));

        given(channelRepository.findAll()).willReturn(List.of(channel,privateChannel1, privateChannel2));

        given(readStatusRepository.findAllByChannelId(any(UUID.class))).willReturn(List.of());
        given(messageRepository.findByChannelId(any(UUID.class))).willReturn(List.of());

        ChannelResponseDto mockResponse1 = mock(ChannelResponseDto.class);
        ChannelResponseDto mockResponse2 = mock(ChannelResponseDto.class);

        given(channelMapper.toDto(eq(channel), any(), anyList(), anyMap())).willReturn(mockResponse1);
        given(channelMapper.toDto(eq(privateChannel1), any(), anyList(), anyMap())).willReturn(mockResponse2);

        // when
        List<ChannelResponseDto> result = channelService.getAllByUserId(userId);

        // then
        assertThat(result).containsExactly(mockResponse1, mockResponse2);
        then(channelMapper).should(never()).toDto(eq(privateChannel2), any(), anyList(), anyMap());
        
    }

    private static void setId(Object domain, UUID id) {
        ReflectionTestUtils.setField(domain, "id", id);
    }

    private static void setCreatedAt(Object domain, Instant createdAt) {
        ReflectionTestUtils.setField(domain, "createdAt", createdAt);
    }
}
