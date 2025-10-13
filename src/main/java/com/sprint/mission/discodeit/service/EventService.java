package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.Event;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface EventService {
    /**
     * 새로운 이벤트를 시스템에 발행(publish)합니다.
     * @param event 발행할 이벤트
     */
    void publishEvent(Event<?> event);

    /**
     * 특정 사용자를 위한 이벤트를 비동기적으로 구독합니다. (Long Polling 핵심)
     * 새로운 이벤트가 발생하거나 타임아웃이 될 때까지 대기합니다.
     * @param userId 이벤트를 구독할 사용자의 ID
     * @return 이벤트 목록을 담고 있는 CompletableFuture
     */
    CompletableFuture<List<Event<?>>> subscribe(UUID userId);
}