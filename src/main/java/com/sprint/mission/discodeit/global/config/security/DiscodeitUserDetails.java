package com.sprint.mission.discodeit.global.config.security;

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

    private final UserResponseDto userResponseDto;
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 유저의 권한(Role)을 리턴하는 곳
        // GrantedAuthority 형태로 변환해서 저장, 접두어로 ROLE_ 를 붙여서 저장
        return List.of(new SimpleGrantedAuthority("ROLE_" + userResponseDto.role()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return userResponseDto.username();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscodeitUserDetails that = (DiscodeitUserDetails) o;
        // DB의 유니크한 ID나 username으로 비교
        return Objects.equals(userResponseDto.username(), that.userResponseDto.username());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userResponseDto.username());
    }

}
