package com.sprint.mission.discodeit.common.security;

import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String un = username == null ? null : username.trim();
        if (un == null || un.isBlank()) {
            throw new UsernameNotFoundException("Invalid username");
        }

        User user = userRepository.findByUsername(un).stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        UserResponseDto dto = userMapper.toDto(user, false);
        return new DiscodeitUserDetails(dto, user.getPassword(), user.getRole());
    }
}
