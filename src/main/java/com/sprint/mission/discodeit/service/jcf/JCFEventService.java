package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.auth.AuthUser;
import com.sprint.mission.discodeit.common.Event;
import com.sprint.mission.discodeit.service.EventService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class JCFEventService implements EventService {

    // 사용자 ID별로 대기 중인 비동기 요청(Future)을 저장합니다.
    private final ConcurrentHashMap<UUID, CompletableFuture<List<Event<?>>>> waitingClients = new ConcurrentHashMap<>();

    // 사용자 ID별로 아직 가져가지 않은 이벤트들을 저장하는 큐입니다.
    private final ConcurrentHashMap<UUID, BlockingQueue<Event<?>>> userEventQueues = new ConcurrentHashMap<>();

    @Override
    public void publishEvent(Event<?> event) {
        System.out.println("새 이벤트 발행: " + event.type() + ", 데이터: " + event.data());

        // 이벤트 종류에 따라 분기
        if (event.type() == Event.EventType.USER_STATUS_CHANGED) {
            AuthUser eventUser = (AuthUser) event.data();

            // 대기중인 모든 클라이언트에게 이벤트 전송 (자기 자신 제외)
            waitingClients.forEach((userId, future) -> {
                if (!userId.equals(eventUser.id()) && !future.isDone()) {
                    future.complete(Collections.singletonList(event));
                }
            });
        }
//        waitingClients.forEach((userId, future) -> {
//            if (!future.isDone()) {
//                future.complete(Collections.singletonList(event));
//            }
//        });
    }

    @Override
    public CompletableFuture<List<Event<?>>> subscribe(UUID userId) {
        CompletableFuture<List<Event<?>>> future = new CompletableFuture<>();
        waitingClients.put(userId, future);

        // 타임아웃 처리 (예: 30초)
        future.orTimeout(30, java.util.concurrent.TimeUnit.SECONDS).whenComplete((result, throwable) -> {
            // 완료되거나 타임아웃되면 맵에서 제거
            waitingClients.remove(userId);
        });

        return future;
    }
}