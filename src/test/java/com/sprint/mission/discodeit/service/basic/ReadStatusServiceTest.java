package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("readstatus 서비스 단위 테스트")
class ReadStatusServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private ReadStatusMapper readStatusMapper;

    @InjectMocks
    private BasicReadStatusService readStatusService;

    @Test
    @DisplayName("정상적으로 readStatusId로 readStatus를 수정할 수 있다")
    void updateReadStatus_Success() {
        // given
        UUID readStatusId = UUID.randomUUID();
        Instant lastReadAt = Instant.now();

        ReadStatus readStatus = new ReadStatus(
                mock(User.class),
                mock(Channel.class),
                lastReadAt
        );
        ReflectionTestUtils.setField(readStatus, "id", readStatusId);

        when(readStatusRepository.findById(readStatus.getId()))
                .thenReturn(Optional.of(readStatus));

        when(readStatusRepository.save(readStatus))
                .thenReturn(readStatus);

        when(readStatusMapper.toResponseDto(readStatus))
                .thenReturn(mock(ReadStatusResponseDto.class));

        UpdateReadStatusRequestDto request = new UpdateReadStatusRequestDto(lastReadAt);

        // when
        ReadStatusResponseDto response = readStatusService.update(readStatus.getId(), request);

        // then
        assertThat(response).isNotNull();

        verify(readStatusRepository, times(1)).findById(readStatus.getId());
        verify(readStatusRepository, times(1)).save(readStatus);
        verify(readStatusMapper, times(1)).toResponseDto(readStatus);
    }

    @Test
    @DisplayName("존재하지않는 readStatusId로는 readstatus를 수정할 수 없다")
    void updateReadStatus_WhenReadStatusDoesNotExist_Fail() {
        // given
        UUID readStatusId = UUID.randomUUID();
        Instant lastReadAt = Instant.now();



        when(readStatusRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        UpdateReadStatusRequestDto request = new UpdateReadStatusRequestDto(lastReadAt);

        // when & then
        assertThatThrownBy(() -> readStatusService.update(readStatusId, request))
                .isInstanceOf(ReadStatusNotFoundException.class);

        verify(readStatusRepository, times(1)).findById(readStatusId);
        verify(readStatusRepository, never()).save(any(ReadStatus.class));
        verify(readStatusMapper, never()).toResponseDto(any(ReadStatus.class));
    }
}