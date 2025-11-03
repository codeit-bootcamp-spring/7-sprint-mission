package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private UUID userId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String pronoun;
    private UUID profileId;

}
