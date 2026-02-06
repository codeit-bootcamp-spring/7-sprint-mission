package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public DiscodeitUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usersRepository.findUserByUsername(username)
            .map(user -> new DiscodeitUserDetails(
                userMapper.toDto(user),
                user.getPassword()
            ))
            .orElseThrow(() ->
                new UsernameNotFoundException("🚨 사용자를 찾을 수 없습니다: " + username)
            );
    }
}
