package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;

public record CreateUserDto(
        String username,
        String password,
        String email,
        String phoneNumber,
        String pronoun,
        CreateBinaryContentDto createBinaryContentDto
) {
}
