package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelDto;
import com.sprint.mission.discodeit.service.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 테스트")
class ChannelServiceTest {
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReadStatusRepository readStatusRepository;
    @Spy
    private ChannelMapper mapper = Mappers.getMapper(ChannelMapper.class);
    @Spy
    private UserMapper userMapper;
    @InjectMocks
    private ChannelService channelService;

    @Nested
    @DisplayName("채널 생성")
    class CreateChannel {
        @Test
        @DisplayName("Public 채널 생성 성공")
        void createChannelSuccess() {
            //given
            PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "test");
            Channel channel = new Channel(
                    request.name(),
                    request.description(),
                    ChannelType.PUBLIC);
            when(channelRepository.save(any(Channel.class)))
                    .thenReturn(channel);

            //when
            ChannelDto channelDto = channelService.createPublicChannel(request);

            //then
            assertThat(channelDto.getName()).isEqualTo(request.name());
            assertThat(channelDto.getDescription()).isEqualTo(request.description());
            assertThat(channelDto.getType()).isEqualTo(ChannelType.PUBLIC);
            verify(mapper).toDto(channel);
            verify(userRepository).findAll();
            verify(readStatusRepository).saveAll(any(List.class));
        }

        @Test
        @DisplayName("Public 채널 생성 실패")
        void createChannelFail() {
            //given
            PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "test");
            when(channelRepository.save(any(Channel.class)))
                    .thenThrow(new RuntimeException("DB error"));

            //when, then
            assertThatThrownBy(()->channelService.createPublicChannel(request))
                    .isInstanceOf(RuntimeException.class);
            verify(channelRepository).save(any(Channel.class));
            verify(userRepository, never()).findAll();
            verify(readStatusRepository, never()).saveAll(any(List.class));
        }

        @Test
        @DisplayName("Private 채널 생성 성공")
        void createPrivateChannel() {
            //given
            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(UUID.randomUUID(), UUID.randomUUID()));
            Channel channel = new Channel(
                    "dm",
                    "description",
                    ChannelType.PRIVATE);
            when(channelRepository.save(any(Channel.class)))
                    .thenReturn(channel);
            when(userRepository.findAllById(request.participantIds()))
                    .thenReturn(List.of(new User("test","1234","1234"), new User("test1","1234","12344")));
            //when
            ChannelDto channelDto = channelService.createPrivateChannel(request);

            //then
            assertThat(channelDto.getName()).isEqualTo("dm");
            assertThat(channelDto.getDescription()).isEqualTo("description");
            assertThat(channelDto.getType()).isEqualTo(ChannelType.PRIVATE);
            verify(mapper).toDto(channel);
            verify(userRepository).findAllById(any(List.class));
            verify(readStatusRepository).saveAll(any(List.class));
        }

        @Test
        @DisplayName("Private 채널 생성 실패")
        void createPrivateChannelFail() {
            //given
            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(UUID.randomUUID(), UUID.randomUUID()));
            when(channelRepository.save(any(Channel.class)))
                    .thenThrow(new RuntimeException("DB error"));

            //when, then
            assertThatThrownBy(()->channelService.createPrivateChannel(request))
                    .isInstanceOf(RuntimeException.class);
            verify(channelRepository).save(any(Channel.class));
            verify(userRepository, never()).findAllById(any(List.class));
            verify(readStatusRepository, never()).saveAll(any(List.class));
        }
    }

    @Nested
    @DisplayName("채널 수정")
    class UpdateChannel {
        @Test
        @DisplayName("채널 수정 성공")
        void channelUpdateSuccess (){
            //given
            UUID channelId = UUID.randomUUID();
            Channel channel = new Channel("test", "test", ChannelType.PUBLIC);
            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.of(channel));
            ChannelUpdateRequest request = new ChannelUpdateRequest("newName", "newDescription");

            //when
            ChannelDto channelDto = channelService.updateChannel(channelId, request);

            //then
            assertThat(channelDto.getName()).isEqualTo(request.newName());
            assertThat(channelDto.getDescription()).isEqualTo(request.newDescription());
            verify(channelRepository).findById(channelId);
         }

        @Test
        @DisplayName("채널 수정 실패")
        void channelUpdateFail (){
            //given
            UUID channelId = UUID.randomUUID();
            Channel channel = new Channel("test", "test", ChannelType.PUBLIC);
            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.empty());
            ChannelUpdateRequest request = new ChannelUpdateRequest("newName", "newDescription");

            //when, then
            assertThatThrownBy(()->channelService.updateChannel(channelId, request))
                    .isInstanceOf(ChannelNotFoundException.class);
            verify(channelRepository).findById(channelId);
        }
    }

    @Nested
    @DisplayName("채널 삭제")
    class DeleteChannel {
        @Test
        @DisplayName("채널 삭제 성공")
        void deleteChannelSuccess (){
            //given
            UUID channelId = UUID.randomUUID();
            Channel channel = new Channel("test", "tests", ChannelType.PRIVATE);
            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.of(channel));
            //when
            channelService.deleteChannel(channelId);

            //then
            verify(channelRepository).findById(channelId);
            verify(channelRepository).delete(channel);
         }

        @Test
        @DisplayName("채널 삭제 실패")
        void deleteChannelFail (){
            //given
            UUID channelId = UUID.randomUUID();

            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.empty());
            //when, then
            assertThatThrownBy(()-> channelService.deleteChannel(channelId))
                    .isInstanceOf(ChannelNotFoundException.class);

            verify(channelRepository).findById(channelId);
            verify(channelRepository, never()).delete(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 조회")
    class FindChannel {
        @Test
        @DisplayName("채널 조회 성공")
        void findChannelSuccess (){
            //given


            //when
//            channelService.

            //then

         }
    }
}