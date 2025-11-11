package com.sprint.mission.discodeit.participation.dto;

import com.sprint.mission.discodeit.config.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.UUID;

public record ParticipationRequestDTO(
        @NotNull(message = "참여하실 채널을 식별 할 수 없습니다.")
        UUID channelId,
        @NotNull(message = "채널 닉네임을 설정해 주세요.")
        String nickname,
        Role role
) {
}
