package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.mapper.dto.UserDto;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {

//    private final User user;
    private final UserDto userDto;
    private final String password;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 유저의 권한(Role)을 리턴하는 곳
//        // GrantedAuthority 형태로 변환해서 저장, 접두어로 ROLE_ 를 붙여서 저장
//        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + String.valueOf(userDto.role())));
    }

    @Override
    public String getPassword() {
        return password;  //userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.username();
    }

//    @Override
//    public boolean isEnabled() {
//        return user.isEnabled();
//    }

    // 나중에 컨트롤러에서 필요할 때 진짜 유저 정보를 꺼내기 위한 getter
    public UserDto getUser() {
        return userDto;
    }

    /*
    크롬 로그인: User 객체 A (ID: user, 주소: 0x10)
    사파리 로그인: User 객체 B (ID: user 주소: 0x20)

    - equals()를 재정의하지 않으면 자바는 주소값으로 비교합니다. ID가 같더라도 주소가 다르기 때문에 서로 다른 계정으로 인식합니다.
     */
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
