package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        String profileUrl
) {
    public static UserDto from(User user) {
        String profileUrl = null;

        BinaryContent profile = user.getProfile();
        if (profile != null) {
            profileUrl = "/api/binaryContents/" + profile.getId() + "/download";
        }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileUrl
        );
    }
}
