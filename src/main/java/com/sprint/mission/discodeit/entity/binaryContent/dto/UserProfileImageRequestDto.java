package com.sprint.mission.discodeit.entity.binaryContent.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserProfileImageRequestDto {
    @NonNull
    private UUID userId;
    private byte[] image;
    private String imageName;
    private String imageType;
}
