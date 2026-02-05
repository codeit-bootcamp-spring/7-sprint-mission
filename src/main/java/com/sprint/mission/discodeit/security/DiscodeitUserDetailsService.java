package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapperManual;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService { // NOTE: 기본 제공되는 DaoAuthenticationProvider로 해도 로직은 똑같기떄문에 커스텀 AuthenticationProvider를 따로 만들진않음, 이 UserDetailService의 사용처는 기본 Provider라는걸 까먹지 않기위해 메모

    private final UserRepository userRepository;
    private final UserMapperManual userMapperManual;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을수 없습니다 : " + username));

        UserResponseDto dto = userMapperManual.toAuthDto(user);
        String password = user.getPassword();
        return new DiscodeitUserDetails(dto, password);
    }
}
