package com.sprint.mission.discodeit.entity.dto.binaryContentDto;

import lombok.*;

import java.util.UUID;

@Builder
public record UserProfileImageUpdateRequest(@NonNull UUID userId,
                                            byte[] image,
                                            String imageName,
                                            String imageType) {
}
