package com.sprint.mission.discodeit.security.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public DiscodeitUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!isUuid(username)){
            User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(username));
            String password = user.getPassword();
            return new DiscodeitUserDetails(userMapper.toDto(user),password);
        }
        UUID userId = UUID.fromString(username);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userId));
        String password = user.getPassword();
        return new DiscodeitUserDetails(userMapper.toDto(user),password);
    }
    private boolean isUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
