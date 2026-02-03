package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {
    private final UserResponseDto userDto;
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userDto.role().name()));
    }

    @Override
    public String getUsername() {
        return userDto.username();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscodeitUserDetails that = (DiscodeitUserDetails) o;
        // DB의 유니크한 ID나 username으로 비교
        return Objects.equals(userDto.username(), that.userDto.username());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDto.username());
    }
}
