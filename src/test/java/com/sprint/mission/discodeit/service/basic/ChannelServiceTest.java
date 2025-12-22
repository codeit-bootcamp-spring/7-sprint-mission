package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNameAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.channel.PrivateChannelUpdateForbiddenException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("채널 서비스 단위 테스트")
class ChannelServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService channelService;

    @Nested
    @DisplayName("공개 채널 생성 테스트")
    class CreatePublicChannelTest {
        @Test
        @DisplayName("정상적으로 공개 채널을 생성할 수 있다")
        void createPublicChannel_Success() {
            // given
            String channelName = "test";
            String description = "description";

            when(channelRepository.save(any(Channel.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(channelMapper.toResponseDto(any(Channel.class)))
                    .thenAnswer(invocation -> {
                        Channel c = invocation.getArgument(0);
                        return new ChannelDto(
                                c.getId(),
                                c.getChannelType(),
                                c.getChannelName(),
                                c.getDescription(),
                                new ArrayList<>(),
                                null
                        );
                    });
            // RequestDto 생성
            CreatePublicChannelRequestDto request =
                    new CreatePublicChannelRequestDto(
                            channelName,
                            description
                    );

            // when
            ChannelDto response = channelService.create(request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.name()).isEqualTo(channelName);
            assertThat(response.description()).isEqualTo(description);

            verify(channelRepository, times(1)).existsByChannelName(channelName);
            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(channelMapper, times(1)).toResponseDto(any(Channel.class));
        }

        @Test
        @DisplayName("중복된 이름으로는 공개 채널을 생성할 수 없다")
        void createChannel_ChannelNameAlreadyExists() {
            // given
            String channelName = "test";
            String description = "description";

            when(channelRepository.existsByChannelName(channelName))
                    .thenReturn(true);

            // RequestDto 생성
            CreatePublicChannelRequestDto request =
                    new CreatePublicChannelRequestDto(
                            channelName,
                            description
                    );
            
            // when
            assertThatThrownBy(() -> channelService.create(request))
                    .isInstanceOf(ChannelNameAlreadyExistsException.class);

            verify(channelRepository, times(1)).existsByChannelName(channelName);
            verify(channelRepository, never()).save(any(Channel.class));
            verify(channelMapper, never()).toResponseDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("비공개 채널 생성 테스트")
    class CreatePrivateChannelTest {

        @Test
        @DisplayName("정상적으로 비공개 채널을 생성할 수 있다")
        void createPrivateChannel_Success() {
            // given
            UUID userId1 = UUID.randomUUID();
            UUID userId2 = UUID.randomUUID();

            User user = new User(
                    "test1",
                    "test1@naver.com",
                    "test1234",
                    null
            );
            ReflectionTestUtils.setField(user, "id", userId1);

            User user2 = new User(
                    "test2",
                    "test2@naver.com",
                    "test1234",
                    null
            );
            ReflectionTestUtils.setField(user2, "id", userId2);

            List<UUID> userList = List.of(userId1, userId2);

            when(userRepository.findById(userId1))
                    .thenReturn(Optional.of(user));

            when(userRepository.findById(userId2))
                    .thenReturn(Optional.of(user2));

            when(channelRepository.save(any(Channel.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(channelMapper.toResponseDto(any(Channel.class)))
                    .thenAnswer(invocation -> {
                        Channel c = invocation.getArgument(0);
                        return new ChannelDto(
                                c.getId(),
                                c.getChannelType(),
                                c.getChannelName(),
                                c.getDescription(),
                                new ArrayList<>(),
                                null
                        );
                    });

            // RequestDto 생성
            CreatePrivateChannelRequestDto request =
                    new CreatePrivateChannelRequestDto(userList);

            // when
            ChannelDto response = channelService.create(request);

            // then
            assertThat(response).isNotNull();

            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(readStatusRepository, times(userList.size())).save(any(ReadStatus.class));
            verify(channelMapper, times(1)).toResponseDto(any(Channel.class));
        }

        @Test
        @DisplayName("존재하지 않는 사용자를 포함한 경우 비공개 채널을 생성할 수 없다")
        void createPrivateChannel_UserNotFound() {
            // given
            UUID userId1 = UUID.randomUUID();
            UUID userId2 = UUID.randomUUID();

            User user = new User(
                    "test1",
                    "test1@naver.com",
                    "test1234",
                    null
            );
            ReflectionTestUtils.setField(user, "id", userId1);

            User user2 = new User(
                    "test2",
                    "test2@naver.com",
                    "test1234",
                    null
            );
            ReflectionTestUtils.setField(user2, "id", userId2);

            List<UUID> userList = List.of(userId1, userId2);

            when(userRepository.findById(userId1))
                    .thenThrow(UserNotFoundException.class);

            // RequestDto 생성
            CreatePrivateChannelRequestDto request =
                    new CreatePrivateChannelRequestDto(userList);

            // when & then
            assertThatThrownBy(() -> channelService.create(request))
                    .isInstanceOf(UserNotFoundException.class);

            verify(channelRepository, never()).save(any(Channel.class));
            verify(readStatusRepository, never()).save(any(ReadStatus.class));
            verify(channelMapper, never()).toResponseDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 수정 테스트")
    class UpdateChannelTest {

        @Test
        @DisplayName("정상적으로 공개 채널을 수정할 수 있다")
        void UpdateChannel_Success() {
            // given
            UUID channelId = UUID.randomUUID();
            String newName = "newName";
            String newDescription = "newDescription";

            ArgumentCaptor<Channel> channelCaptor = ArgumentCaptor.forClass(Channel.class);

            Channel beforeChannel = new Channel(
                    "beforeName",
                    ChannelType.PUBLIC,
                    "beforeDescription"
            );

            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.of(beforeChannel));

            when(channelRepository.save(any(Channel.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(channelMapper.toResponseDto(any(Channel.class)))
                    .thenAnswer(invocation -> {
                        Channel c = invocation.getArgument(0);
                        return new ChannelDto(
                                c.getId(),
                                c.getChannelType(),
                                c.getChannelName(),
                                c.getDescription(),
                                new ArrayList<>(),
                                null
                        );
                    });

            // RequestDto 생성
            UpdatePublicChannelRequestDto request =
                    new UpdatePublicChannelRequestDto(newName, newDescription);

            // when
            ChannelDto response = channelService.update(channelId, request);

            // then
            assertThat(response).isNotNull();

            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, times(1)).save(channelCaptor.capture());
            verify(channelMapper, times(1)).toResponseDto(any(Channel.class));

            Channel captureChannel = channelCaptor.getValue();

            assertThat(captureChannel.getChannelName()).isEqualTo(newName);
            assertThat(captureChannel.getDescription()).isEqualTo(newDescription);
        }

        @Test
        @DisplayName("비공개 채널은 수정할 수 없다")
        void updatePrivateChannel_PrivateChannelUpdateForbidden() {
            // given
            UUID channelId = UUID.randomUUID();
            String newName = "newName";
            String newDescription = "newDescription";

            Channel channel = new Channel(
                    null,
                    ChannelType.PRIVATE,
                    null
            );

            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.of(channel));

            // RequestDto 생성
            UpdatePublicChannelRequestDto request =
                    new UpdatePublicChannelRequestDto(newName, newDescription);

            // when & then
            assertThatThrownBy(() -> channelService.update(channelId, request))
                    .isInstanceOf(PrivateChannelUpdateForbiddenException.class);

            // then
            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, never()).save(any(Channel.class));
            verify(channelMapper, never()).toResponseDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 삭제 테스트")
    class DeleteChannelTest {

        @Test
        @DisplayName("정상적으로 채널을 삭제할 수 있다")
        void deleteChannel_Success() {
            // given
            UUID channelId = UUID.randomUUID();

            Channel channel = new Channel(
                    "beforeName",
                    ChannelType.PUBLIC,
                    "beforeDescription"
            );

            List<Message> messages = List.of(
                    new Message("1", channel, null, new ArrayList<>()),
                    new Message("2", channel, null, new ArrayList<>()),
                    new Message("3", channel, null, new ArrayList<>()),
                    new Message("4", channel, null, new ArrayList<>()),
                    new Message("5", channel, null, new ArrayList<>()),
                    new Message("6", channel, null, new ArrayList<>())
            );
            messages.forEach(message -> {
                ReflectionTestUtils.setField(message, "id", UUID.randomUUID());
            });

            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.of(channel));

            when(messageRepository.findAllByChannelId(channelId))
                    .thenReturn(messages);

            // when
            channelService.delete(channelId);

            // then
            verify(channelRepository, times(1)).findById(channelId);
            verify(readStatusRepository, times(1)).deleteByChannelId(channelId);
            verify(binaryContentRepository, times(1)).deleteByIdIn(anyList());
            verify(channelRepository, times(1)).deleteById(channelId);
        }

        @Test
        @DisplayName("존재하지 않는 채널은 삭제할 수 없다")
        void deleteChannel_ChannelNotFound() {
            // given
            UUID channelId = UUID.randomUUID();

            Channel channel = new Channel(
                    "beforeName",
                    ChannelType.PUBLIC,
                    "beforeDescription"
            );

            when(channelRepository.findById(channelId))
                    .thenThrow(ChannelNotFoundException.class);
            // when & then
            assertThatThrownBy(() -> channelService.delete(channelId))
                    .isInstanceOf(ChannelNotFoundException.class);

            verify(channelRepository, times(1)).findById(channelId);
            verify(readStatusRepository, never()).deleteByChannelId(channelId);
            verify(binaryContentRepository, never()).deleteByIdIn(anyList());
            verify(channelRepository, never()).deleteById(channelId);
        }
    }

    @Nested
    @DisplayName("채널 조회 테스트")
    class FindChannelTest {
        @Test
        @DisplayName("사용자 아이디로 채널을 조회할 수 있다")
        void findChannel_Success() {
            // given
            UUID userId = UUID.randomUUID();

            Channel publicChannel = new Channel(
                    "publicChannel",
                    ChannelType.PUBLIC,
                    "공개 채널"
            );

            Channel privateChannel = new Channel(
                    null,
                    ChannelType.PRIVATE,
                    null
            );

            Channel privateChannel2 = new Channel(
                    null,
                    ChannelType.PRIVATE,
                    null
            );

            ReadStatus readStatus = new ReadStatus(
                    null, privateChannel, null
            );

            ReadStatus readStatus2 = new ReadStatus(
                    null, privateChannel2, null
            );

            when(channelRepository.findAllByChannelType(ChannelType.PUBLIC))
                    .thenReturn(List.of(publicChannel));

            when(readStatusRepository.findAllByUserIdWithChannel(userId))
                    .thenReturn(List.of(readStatus, readStatus2));

            when(channelMapper.toResponseDto(any(Channel.class)))
                    .thenAnswer(invocation -> {
                        Channel c = invocation.getArgument(0);
                        return new ChannelDto(
                                c.getId(),
                                c.getChannelType(),
                                c.getChannelName(),
                                null,
                                new ArrayList<>(),
                                null
                        );
                    });

            // when
            List<ChannelDto> response = channelService.findAllByUserId(userId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.size()).isEqualTo(3);

            verify(channelRepository, times(1)).findAllByChannelType(ChannelType.PUBLIC);
            verify(readStatusRepository, times(1)).findAllByUserIdWithChannel(userId);
            verify(channelMapper, times(response.size())).toResponseDto(any(Channel.class));
        }
    }
}