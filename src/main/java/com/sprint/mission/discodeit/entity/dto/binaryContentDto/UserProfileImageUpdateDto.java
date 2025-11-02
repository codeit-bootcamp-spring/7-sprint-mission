package com.sprint.mission.discodeit.entity.dto.binaryContentDto;

import lombok.*;

import java.util.UUID;

@Builder
public record UserProfileImageUpdateDto(@NonNull UUID userId,
                                        byte[] image,
                                        String imageName,
                                        String imageType) {
}
