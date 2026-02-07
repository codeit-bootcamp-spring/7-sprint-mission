package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public record UserUpdateCommand (
        UUID id,
        String username,
        String email,
        String password,
        Optional<BinaryContentUploadCommand> profile
) {

    public static UserUpdateCommand from(UUID id , UserUpdateRequestDto requestDto, MultipartFile profileFile) {
        Optional<BinaryContentUploadCommand> profile = Optional.ofNullable(profileFile)
                .filter(file -> !file.isEmpty())
                .map(BinaryContentUploadCommand::from);
        return new UserUpdateCommand(
                id,
                requestDto.newUsername(),
                requestDto.newEmail(),
                requestDto.newPassword(),
                profile
        );

    }
}
