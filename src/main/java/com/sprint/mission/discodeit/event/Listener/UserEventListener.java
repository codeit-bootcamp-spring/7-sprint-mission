package com.sprint.mission.discodeit.event.Listener;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.UserUpdatedEvent;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventListener {

    private final SseService  sseService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleUserUpdatedEvent(UserUpdatedEvent userUpdatedEvent) {


        User user = userRepository.findById(userUpdatedEvent.userId()).orElseThrow();
    }
}
