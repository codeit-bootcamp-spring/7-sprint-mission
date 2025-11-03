package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserRepository userRepository;

    public boolean checkUserOnline(UUID userId){
        User user = UserFindHelper.findById(userRepository,userId);
        return user.checkOnline();
    }
}
