package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Override
    public DiscodeitUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usersRepository.findUserByUsername(username)
            .map(user -> new DiscodeitUserDetails(
                userMapper.toDto(user),
                user.getPassword()
            ))
            .orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username)
            );
    }
}
