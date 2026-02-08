package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private BinaryContentDto profile;
    private boolean online;
    private Role role;

    public static UserDto from(User user){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true,
                user.getRole()
        );
    }
}
