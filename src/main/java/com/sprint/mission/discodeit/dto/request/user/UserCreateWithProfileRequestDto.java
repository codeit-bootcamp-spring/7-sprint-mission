package com.sprint.mission.discodeit.dto.request.user;


import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;

public record UserCreateWithProfileRequestDto(
        UserCreateRequestDto userInfo,
        ProfileCreateRequestDto profileInfo
) {}
