package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.response.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReadStatusService Unit Test")
public class ReadStatusServiceUnitTest {

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ReadStatusMapper readStatusMapper;

    @InjectMocks
    private BasicReadStatusService readStatusService;

    private ReadStatus readStatus;
    private User user;
    private Channel channel;
    private UUID userId;
    private UUID channelId;
    private ReadStatusDto readStatusDto;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId = UUID.randomUUID();
        user = User.createUserFactory("user1","111@user","1234");
        ReflectionTestUtils.setField(user,"id",userId);

        channel = Channel.privateChannelFactory("privateChannel","privateChannelDesc");
        ReflectionTestUtils.setField(channel,"id",channelId);
        readStatus = ReadStatus.createReadStatusFactory(user,channel);
        readStatusDto = new ReadStatusDto(
                UUID.randomUUID(),
                userId,
                channelId,
                Instant.now()
        );
    }

    @Test
    @DisplayName("[정상 케이스] readstatus 생성 성공")
    void createReadStatus_Success(){

        given(channelRepository.findById(any(UUID.class))).willReturn(Optional.of(channel));
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));
        given(readStatusRepository.save(any(ReadStatus.class))).willReturn(readStatus);
        given(readStatusMapper.toDto(any(ReadStatus.class))).willReturn(readStatusDto);

        ReadStatusDto response = readStatusService.createReadStatus(TestFixture.readStatusCreateFactory(channelId, userId));

        assertThat(response.id()).isEqualTo(readStatusDto.id());
        assertThat(response.userId()).isEqualTo(readStatusDto.userId());
        assertThat(response.channelId()).isEqualTo(readStatusDto.channelId());

        then(readStatusRepository).should(times(1)).save(any(ReadStatus.class));
        then(readStatusMapper).should(times(1)).toDto(any(ReadStatus.class));
    }

    @Test
    @DisplayName("[정상 케이스] readstatus 삭제 성공")
    void deleteReadStatus_Success(){
        doNothing().when(readStatusRepository).deleteById(any(UUID.class));
        readStatusService.deleteReadStatus(readStatusDto.id());
        then(readStatusRepository).should(times(1)).deleteById(any(UUID.class));

    }

    @Test
    @DisplayName("[정상 케이스] readstatus 조회 성공")
    void readReadStatus_Success(){

        given(readStatusRepository.findAll()).willReturn(List.of(readStatus));
        given(readStatusMapper.toDto(any(ReadStatus.class))).willReturn(readStatusDto);

        List<ReadStatusDto> response = readStatusService.findAllyByUserId(userId);

        assertThat(response).containsExactly(readStatusDto);

        then(readStatusMapper).should(times(1)).toDto(any(ReadStatus.class));
    }

    @Test
    @DisplayName("[정상 케이스] 유저 변경 성공")
    void patchReadStatus_Success(){

        given(readStatusRepository.findById(any(UUID.class))).willReturn(Optional.of(readStatus));
        given(readStatusRepository.save(any(ReadStatus.class))).willReturn(readStatus);
        given(readStatusMapper.toDto(any(ReadStatus.class))).willReturn(readStatusDto);

        ReadStatusDto response = readStatusService.patchReadStatus(UUID.randomUUID(), TestFixture.readStatusPatchFactory());

        assertThat(response).isEqualTo(readStatusDto);

        then(readStatusRepository).should(times(1)).save(any(ReadStatus.class));
        then(readStatusMapper).should(times(1)).toDto(any(ReadStatus.class));


    }
}
