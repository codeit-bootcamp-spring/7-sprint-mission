package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Channel Service 테스트")
class BasicChannelServiceTest {
    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService channelService;

    private User user1;
    private User user2;
    private UserResponseDto userResponseDto1;
    private UserResponseDto userResponseDto2;
    private Channel publicChannel;
    private Channel privateChannel;
    private CreatePublicChannelDto createPublicChannelDto;
    private CreatePrivateChannelDto createPrivateChannelDto;
    private ChannelResponseDto publicChannelResponseDto;
    private ChannelResponseDto privateChannelResponseDto;
    private UpdateChannelDto updateChannelDto;

    @BeforeEach
    void setUp() {
        user1 = new User("test1", "test1@codiet.com", "test_123", null);
        user2 = new User("test2", "test2@codiet.com", "test_456", null);

        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        ReflectionTestUtils.setField(user1, "id", userId1);
        ReflectionTestUtils.setField(user2, "id", userId2);

        createPublicChannelDto = new CreatePublicChannelDto("test", "test channel");
        createPrivateChannelDto = new CreatePrivateChannelDto(List.of(userId1, userId2));
        updateChannelDto = new UpdateChannelDto("test_new", "test_new channel");

        publicChannel = Channel.builder()
                .type(ChannelType.PUBLIC)
                .name(createPublicChannelDto.name())
                .description(createPublicChannelDto.description())
                .build();

        privateChannel = Channel.builder()
                .type(ChannelType.PRIVATE)
                .build();

        UUID publicChannelId = UUID.randomUUID();
        UUID privateChannelId = UUID.randomUUID();
        ReflectionTestUtils.setField(publicChannel, "id", publicChannelId);
        ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);

        publicChannelResponseDto = new ChannelResponseDto(
                publicChannelId,
                publicChannel.getType(),
                publicChannel.getName(),
                publicChannel.getDescription(),
                List.of(),
                Instant.MIN
        );


        userResponseDto1 = new UserResponseDto(user1.getId(), user1.getUsername(), user1.getEmail(), null, true);
        userResponseDto2 = new UserResponseDto(user2.getId(), user2.getUsername(), user2.getEmail(), null, true);


        privateChannelResponseDto = new ChannelResponseDto(
                privateChannelId,
                privateChannel.getType(),
                null,
                null,
                List.of(userResponseDto1, userResponseDto2),
                Instant.MIN
        );
    }

    @Nested
    @DisplayName("채널 생성 테스트")
    class ChannelCreate {
        @Test
        @DisplayName("[정상 케이스] - Public Channel 생성")
        void createPublicChannel_success() {
            // given
            given(channelRepository.save(any(Channel.class)))
                    .willReturn(publicChannel);
            given(channelMapper.toResponseDto(any(Channel.class)))
                    .willReturn(publicChannelResponseDto);

            // when
            ChannelResponseDto result = channelService.createChannel(createPublicChannelDto);

            // then
            assertThat(result).isEqualTo(publicChannelResponseDto);
            assertThat(result.id()).isEqualTo(publicChannelResponseDto.id());
            assertThat(result.type()).isEqualTo(publicChannelResponseDto.type());
            assertThat(result.name()).isEqualTo(publicChannelResponseDto.name());

            then(channelRepository).should().save(any(Channel.class));
            then(channelMapper).should().toResponseDto(any(Channel.class));
        }

        @Test
        @DisplayName("[정상 케이스] - Private Channel 생성")
        void createPrivateChannel_success() {
            // given
            given(channelRepository.save(any(Channel.class)))
                    .willReturn(privateChannel);
            given(userRepository.findAllById(anyList()))
                    .willReturn(List.of(user1, user2));
            given(readStatusRepository.saveAll(anyList()))
                    .willReturn(List.of());
            given(channelMapper.toResponseDto(any(Channel.class)))
                    .willReturn(privateChannelResponseDto);

            // when
            ChannelResponseDto result = channelService.createChannel(createPrivateChannelDto);

            // then
            assertThat(result).isEqualTo(privateChannelResponseDto);
            assertThat(result.id()).isEqualTo(privateChannelResponseDto.id());
            assertThat(result.type()).isEqualTo(privateChannelResponseDto.type());
            assertThat(result.name()).isEqualTo(privateChannelResponseDto.name());

            then(channelRepository).should().save(any(Channel.class));
            then(userRepository).should().findAllById(anyList());
            then(channelMapper).should().toResponseDto(any(Channel.class));
            then(readStatusRepository).should().saveAll(anyList());
        }

        @Test
        @DisplayName("[예외 케이스] - Private Channel 존재하지 않는 사용자 참여")
        void createPrivateChannel_notFoundUser_fail() {
            CreatePrivateChannelDto createPrivateChannelDto1 = new CreatePrivateChannelDto(
                    List.of(user1.getId(), UUID.randomUUID())
            );

            // given
            given(userRepository.findAllById(anyList()))
                    .willReturn(List.of(user1));


            // when
            assertThatThrownBy(() -> channelService.createChannel(createPrivateChannelDto1))
                    .isInstanceOf(UserNotFoundException.class);

            // then
            then(userRepository).should().findAllById(anyList());
            then(channelRepository).should(never()).save(any(Channel.class));
            then(readStatusRepository).should(never()).saveAll(anyList());
            then(channelMapper).should(never()).toResponseDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 조회 테스트")
    class ChannelRead {
        @Test
        @DisplayName("[정상 케이스] - Public Channel 조회")
        void readPublicChannel_Success() {
            // given
            given(channelRepository.findById(any()))
                    .willReturn(Optional.of(publicChannel));
            given(channelMapper.toResponseDto(any(Channel.class)))
                    .willReturn(publicChannelResponseDto);

            // when
            ChannelResponseDto result = channelService.getChannel(publicChannel.getId());

            // then
            assertThat(result).isEqualTo(publicChannelResponseDto);
            assertThat(result.id()).isEqualTo(publicChannelResponseDto.id());
            assertThat(result.name()).isEqualTo(publicChannelResponseDto.name());

            then(channelRepository).should().findById(any());
            then(channelMapper).should().toResponseDto(any(Channel.class));
        }

        @Test
        @DisplayName("[정상 케이스] - Private Channel 조회")
        void readPrivateChannel_Success() {

            given(channelRepository.findById(any()))
                    .willReturn(Optional.of(privateChannel));
            given(channelMapper.toResponseDto(any(Channel.class)))
                    .willReturn(privateChannelResponseDto);

            // when
            ChannelResponseDto result = channelService.getChannel(privateChannel.getId());

            // then
            assertThat(result).isEqualTo(privateChannelResponseDto);
            assertThat(result.id()).isEqualTo(privateChannelResponseDto.id());
            assertThat(result.name()).isEqualTo(null);
            assertThat(result.description()).isEqualTo(null);


            then(channelRepository).should().findById(any());
            then(channelMapper).should().toResponseDto(any(Channel.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 채널 조회")
        void readChannel_fail() {
            // given
            given(channelRepository.findById(any(UUID.class)))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> channelService.getChannel(UUID.randomUUID()))
                    .isInstanceOf(ChannelNotFoundException.class);

            then(channelRepository).should().findById(any());
            then(channelMapper).should(never()).toResponseDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("특정 유저의 채널 목록 조회 테스트")
    class ReadChannelByUser {
        @Test
        @DisplayName("[정상 케이스] - 유저의 채널 목록 조회")
        void readChannelByUser_Success() {
            // given
            ReadStatus readStatus = ReadStatus.builder()
                    .user(user1)
                    .channel(publicChannel)
                    .lastReadAt(Instant.now())
                    .build();
            ReadStatus readStatus2 = ReadStatus.builder()
                    .user(user1)
                    .channel(privateChannel)
                    .lastReadAt(Instant.now())
                    .build();

            // 참여한 채널이 1개, (public Channel 참여)
            given(readStatusRepository.findAllByUserIdWithChannel(user1.getId()))
                    .willReturn(List.of(readStatus, readStatus2));
            given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of(publicChannel.getId(), privateChannel.getId())))
                    .willReturn(List.of(publicChannel, privateChannel));

            given(channelMapper.toResponseDto(publicChannel))
                    .willReturn(publicChannelResponseDto);
            given(channelMapper.toResponseDto(privateChannel))
                    .willReturn(privateChannelResponseDto);

            // when
            List<ChannelResponseDto> result = channelService.getAllChannelByUserId(user1.getId());

            // then
            assertThat(result).hasSize(2);
            /**
             * NOTE: containsExactly: 리스트의 내용을 검증할 때 가장 까다롭고 엄격한 검증 방식
             * 1. 내용물(Element)이 일치하는가?
             * 2. 순서(Order)가 정확한가?
             * 3. 개수(Size)가 정확한가?
             */
            assertThat(result).containsExactly(publicChannelResponseDto, privateChannelResponseDto);

            // 검증: Repository들이 의도한 인자로 호출되었는가
            then(readStatusRepository).should().findAllByUserIdWithChannel(user1.getId());
            then(channelRepository).should().findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList());
            then(channelMapper).should(times(2)).toResponseDto(any(Channel.class));
        }

        @Test
        @DisplayName("[정상 케이스] - 참여 채널이 없는 유저 (PUBLIC 채널만 조회)")
        void getAllChannelByUserId_noPrivateChannels_success() {
            // given

            given(readStatusRepository.findAllByUserIdWithChannel(user1.getId()))
                    .willReturn(List.of());
            given(channelRepository.findAllByTypeOrIdIn(any(ChannelType.class), anyList()))
                    .willReturn(List.of(publicChannel));
            given(channelMapper.toResponseDto(any(Channel.class)))
                    .willReturn(publicChannelResponseDto);

            // when
            List<ChannelResponseDto> result = channelService.getAllChannelByUserId(user1.getId());

            // then
            assertThat(result).hasSize(1);

            then(readStatusRepository).should().findAllByUserIdWithChannel(user1.getId());
            then(channelRepository).should().findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());
            then(channelMapper).should().toResponseDto(any(Channel.class));
        }

    }

    @Nested
    @DisplayName("채널 업데이트 테스트")
    class UpdateChannel {
        @Test
        @DisplayName("[정상 케이스] - 채널 업데이트 성공")
        void publicChannelUpdate_success() {
            // given
            given(channelRepository.findById(publicChannel.getId()))
                    .willReturn(Optional.ofNullable(publicChannel));

            ChannelResponseDto updatedChannelResponseDto = new ChannelResponseDto(
                    publicChannel.getId(),
                    publicChannel.getType(),
                    updateChannelDto.newName(),
                    updateChannelDto.newDescription(),
                    List.of(),
                    Instant.MIN
            );

            given(channelMapper.toResponseDto(any(Channel.class)))
                    .willReturn(updatedChannelResponseDto);


            // when
            ChannelResponseDto result = channelService.updateChannel(publicChannel.getId(), updateChannelDto);

            // then
            assertThat(publicChannel.getName()).isEqualTo(updateChannelDto.newName());
            assertThat(publicChannel.getDescription()).isEqualTo(updateChannelDto.newDescription());

            assertThat(result).isEqualTo(updatedChannelResponseDto);
            assertThat(result.type()).isEqualTo(updatedChannelResponseDto.type());
            assertThat(result.id()).isEqualTo(updatedChannelResponseDto.id());
            assertThat(result.description()).isEqualTo(updatedChannelResponseDto.description());
            assertThat(result.name()).isEqualTo(updatedChannelResponseDto.name());
            assertThat(result.participants()).hasSize(0);


            then(channelRepository).should().findById(publicChannel.getId());
            then(channelMapper).should().toResponseDto(publicChannel);
        }

        @Test
        @DisplayName("[예외 케이스] - 채널 업데이트 실패(존재 하지 않는 채널)")
        void updateChannel_notFound_Fail() {
            // given
            UUID randomId = UUID.randomUUID();
            given(channelRepository.findById(randomId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> channelService.updateChannel(randomId, updateChannelDto))
                    .isInstanceOf(ChannelNotFoundException.class);

            // then
            then(channelRepository).should().findById(randomId);
            then(channelMapper).should(never()).toResponseDto(any(Channel.class));
        }

        @Test
        @DisplayName("[예외 케이스] - Private 채널 업데이트")
        void updatePrivateChannel_fail() {
            // given
            given(channelRepository.findById(privateChannel.getId()))
                    .willReturn(Optional.ofNullable(privateChannel));
            // when
            assertThatThrownBy(() -> channelService.updateChannel(privateChannel.getId(), updateChannelDto))
                    .isInstanceOf(PrivateChannelUpdateException.class);

            // then
            then(channelRepository).should().findById(privateChannel.getId());
            then(channelMapper).should(never()).toResponseDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 삭제 테스트")
    class DeleteChannel {
        @Test
        @DisplayName("[정상 케이스] 삭제 테스트")
        void deleteChannel_success() {
            // given
            given(channelRepository.findById(publicChannel.getId()))
                    .willReturn(Optional.ofNullable(publicChannel));

            willDoNothing().given(channelRepository).delete(publicChannel);
            willDoNothing().given(readStatusRepository).deleteAllByChannelId(publicChannel.getId());
            willDoNothing().given(messageRepository).deleteAllByChannelId(publicChannel.getId());
            // when

            channelService.deleteChannel(publicChannel.getId());

            // then
            then(channelRepository).should().findById(publicChannel.getId());
            then(channelRepository).should().delete(publicChannel);
            then(readStatusRepository).should().deleteAllByChannelId(publicChannel.getId());
            then(messageRepository).should().deleteAllByChannelId(publicChannel.getId());
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 채널 삭제")
        void deleteChannel_notFound_fail() {
            // given
            given(channelRepository.findById(publicChannel.getId()))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> channelService.deleteChannel(publicChannel.getId()))
                    .isInstanceOf(ChannelNotFoundException.class);

            // then
            then(channelRepository).should().findById(publicChannel.getId());
            then(channelRepository).should(never()).delete(publicChannel);
            then(readStatusRepository).should(never()).deleteAllByChannelId(publicChannel.getId());
            then(messageRepository).should(never()).deleteAllByChannelId(publicChannel.getId());

        }
    }

}
