package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.entity.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {
    @Mock
    private Channel channel;

    @Mock
    private User user;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @InjectMocks
    private BasicChannelService channelService;

    @Test
    void 공개_채널_생성_성공() {
        //given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("음악", "음놀해요");
        when(channel.getId()).thenReturn(UUID.randomUUID());


        //when
        ChannelDto channelDto = channelService.createPublicChannel(request);

        //then
        verify(channelRepository.save(any()));
        assertThat(channelDto.name()).isEqualTo(request.name());
        assertThat(channelDto.description()).isEqualTo(request.description());
    }

    @Test
    void 비공개_채널_생성_성공() {

        // given
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();
        User user1 = new User("희연", "hee123", "hee11@gmail.com");
        User user2 = new User("정원", "garden", "garden@gmail.com");
        User user3 = new User("건우", "hes123", "he@gmail.com");
        when(channel.getId()).thenReturn(UUID.randomUUID());
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(userRepository.findById(uuid1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(uuid2)).thenReturn(Optional.of(user2));
        when(userRepository.findById(uuid3)).thenReturn(Optional.of(user3));
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(Set.of(uuid1, uuid2, uuid3));
        when(channelRepository.save(any())).thenReturn(Channel.createPrivateChannel(Set.of(user1, user2, user3)));


        // when
        ChannelDto response = channelService.createPrivateChannel(request);

        // then
        verify(userRepository).findById(uuid1);
        verify(userRepository).findById(uuid2);
        verify(userRepository).findById(uuid3);
        verify(channelRepository).save(any());
        assertThat(response).isNotNull();
        assertThat(response.participants()).isNotNull();
        assertThat(response.participants()).hasSize(3);
        assertThat(response.participants().stream().map(UserDto::username).toList()).containsExactlyInAnyOrder("정원", "희연", "건우");
    }

    @Test
    void 채널_생성_실패() {

    }

    @Test
    void 공개_채널_업데이트() {
    }

    @Test
    void 공개_채널_업데이트_실패() {

    }

    @Test
    void 채널_삭제() {
    }

    @Test
    void 채널_삭제_실패() {

    }
}