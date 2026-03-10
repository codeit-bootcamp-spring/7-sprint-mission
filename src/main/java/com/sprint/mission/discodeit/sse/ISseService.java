package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.sse.model.SseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ISseService implements SseService {

    private static final int CLEAN_DELAY = 1000 * 60 * 30;
    private final Long TIMEOUT = 1000 * 60 * 60L;
    private final Map<UUID, SseEmitter> userEmitters = new ConcurrentHashMap<>();
    private final SseEmitterRepository sseEmitterRepository;
    private final Map<UUID, Instant> lastActivityTime = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connect(UUID receiverId, UUID lastEventId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        if (userEmitters.containsKey(receiverId)) {
            SseEmitter oldEmitter = userEmitters.get(receiverId);
            oldEmitter.complete();

        }
        userEmitters.put(receiverId, emitter);
        emitter.onCompletion(() -> {
            userEmitters.remove(receiverId);
            log.info("SSE 연결 완료 - 사용자: {}, 남은 연결 수: {}", receiverId, userEmitters.size());
        });

        emitter.onTimeout(() -> {
            userEmitters.remove(receiverId);
            log.info("SSE 연결 타임아웃 - 사용자: {}, 남은 연결 수: {}", receiverId, userEmitters.size());
        });

        emitter.onError((e) -> {
            userEmitters.remove(receiverId);
            log.info("SSE 연결 에러 - 사용자: {}, 에러: {}", receiverId, e.getMessage());
        });

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connect")
                            .data("SSE 연결이 수립되었습니다.")
            );

            restoreMissedEvents(receiverId, lastEventId, emitter);

        } catch (IOException e) {
            log.error("초기 메시지 전송 실패 - 사용자: {}", receiverId, e);
            userEmitters.remove(receiverId);
            emitter.completeWithError(e);
        }
        return emitter;
    }

    private void restoreMissedEvents(UUID receiverId, UUID lastEventId, SseEmitter emitter) {
        if (lastEventId == null) {
            return;
        }

        try {
            List<SseEvent> missedEvents = sseEmitterRepository.getEventSince(receiverId, lastEventId);
            for (SseEvent event : missedEvents) {
                emitter.send(SseEmitter.event()
                        .id(event.eventId().toString())
                        .name(event.eventName())
                        .data(event.data())
                );
            }

        } catch (IOException e) {
            log.error("이벤트 복원 실패 : {}", e.getMessage());
        }

    }

    @Override
    public void send(Collection<UUID> receiverIds, String eventName, Object data) {
        for (UUID receiverId : receiverIds) {

            UUID eventId = sseEmitterRepository.saveEvent(receiverId, eventName, data);
            SseEmitter emitter = userEmitters.get(receiverId);
            if (emitter == null) {
                log.info("SSE 연결을 찾을 수 없습니다ㅑ - 사용자 : {}", receiverId);
                return;
            }
            try {
                emitter.send(
                        SseEmitter.event()
                                .id(eventId.toString())
                                .name(eventName)
                                .data(data)
                );
                log.info("알림 전송 성공 - 사용자 : {}, 이벤트 : {}", receiverId, eventName);
            } catch (IOException e) {
                log.error("알림 전송 실패 - 사용자: {}, 이벤트: {}", receiverId, eventName, e);
                userEmitters.remove(receiverId);
                emitter.completeWithError(e);
            }
        }
    }

    @Override
    public void broadcast(String eventName, Object data) {
        log.info("브로드캐스트 시작 - 이벤트: {}, 대상: {}명", eventName, userEmitters.size());

        userEmitters.forEach((receiverId, emitter) -> {

            try {
                emitter.send(
                        SseEmitter.event()
                                .name(eventName)
                                .data(data)
                );
                log.info("알림 전송 성공 - 사용자: {}, 이벤트: {}", receiverId, eventName);
            } catch (IOException e) {
                log.error("알림 전송 실패 - 사용자: {}, 이벤트: {}", receiverId, eventName, e);
                userEmitters.remove(receiverId);
                emitter.completeWithError(e);
            }

        });
    }

    @Override
    @Scheduled(fixedDelay = CLEAN_DELAY)
    public void cleanUp() {


        userEmitters.keySet().forEach(userId -> {
        SseEmitter emitter = userEmitters.get(userId);
        if(!ping(userId, emitter)){
            lastActivityTime.remove(userId);
            userEmitters.remove(userId);
            emitter.complete();
            log.info("비활성 SSE 연결 정리 - 사용자 :{}",userId);
        }

        });

        // 모든 활성 연결에 ping 전송

    }

    @Override
    public boolean ping(UUID userId, SseEmitter sseEmitter) {
        Instant now = Instant.now();
        Instant lastActivity = lastActivityTime.get(userId);
        if (lastActivity != null && lastActivity.plus(5, ChronoUnit.MINUTES).isBefore(now)) return false;
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .name("ping")
                            .data("ping")
            );
            lastActivityTime.put(userId, Instant.now());
            log.debug("ping 전송 성공 - 사용자 : {}", userId);
            return true;
        } catch (IOException e) {
            log.error("ping 전송 실패 - 사용자 - {}", userId, e);
            return false;
        }
    }

    public void closeEmitter(String userId) {
        SseEmitter emitter = userEmitters.get(userId);
        if (emitter != null) {
            emitter.complete();
            userEmitters.remove(userId);
            log.info("SSE 연결 수동 종료 - 사용자: {}", userId);
        }
    }
}
