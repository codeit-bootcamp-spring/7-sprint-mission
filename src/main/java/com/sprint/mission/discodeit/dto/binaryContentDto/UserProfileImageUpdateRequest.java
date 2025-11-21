package com.sprint.mission.discodeit.dto.binaryContentDto;

import lombok.*;

import java.util.UUID;

@Builder
public record UserProfileImageUpdateRequest(@NonNull UUID userId,
                                            byte[] image,
                                            String imageName,
                                            String imageType) {
}
