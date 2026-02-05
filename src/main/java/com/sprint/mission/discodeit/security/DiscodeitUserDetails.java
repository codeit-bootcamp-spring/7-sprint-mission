package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.type.Role;
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
        // 유저의 권한은(Role)을 리턴하는 곳
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

    public Role getRole() {
        return userResponseDto.role();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        DiscodeitUserDetails that = (DiscodeitUserDetails) obj;
        return Objects.equals(userResponseDto.username(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userResponseDto.username());
    }
}
