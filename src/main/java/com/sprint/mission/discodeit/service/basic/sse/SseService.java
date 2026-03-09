package com.sprint.mission.discodeit.service.basic.sse;

import com.sprint.mission.discodeit.repository.sse.SseEmitterRepository;
import com.sprint.mission.discodeit.repository.sse.SseMessage;
import com.sprint.mission.discodeit.repository.sse.SseMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseService {

    private static final Long TIMEOUT = 1000L * 60 * 60;

    private final SseEmitterRepository emitterRepository;
    private final SseMessageRepository sseMessageRepository;

    public SseEmitter connect(UUID receiverId, UUID lastEventId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitterRepository.save(receiverId, emitter);

        emitter.onCompletion(() -> {
            emitterRepository.delete(receiverId, emitter);
            log.info("SSE 연결 완료 - 사용자: {}, 남은 연결 수: {}", receiverId, emitter);
        });

        emitter.onTimeout(() -> {
            emitterRepository.delete(receiverId, emitter);
            log.info("SSE 연결 타임아웃 - 사용자: {}, 남은 연결 수: {}", receiverId, emitter);
        });

        emitter.onError(e -> {
            emitterRepository.delete(receiverId, emitter);
            log.info("SSE 연결 에러 - 사용자: {}, 에러: {}", receiverId, e.getMessage());
        });

        ping(emitter);

        if (lastEventId != null) {
            List<SseMessage> missedMessages = sseMessageRepository.findEventSince(lastEventId);
            for (SseMessage message : missedMessages) {
                sendToUser(receiverId, emitter, message.eventId(), message.eventName(), message.data());
            }
        }

        return emitter;
    }

    public void send(Collection<UUID> receiverIds, String eventName, Object data) {
        UUID eventId = UUID.randomUUID();
        sseMessageRepository.save(eventId, new SseMessage(eventId, eventName, data));

        for (UUID receiverId : receiverIds) {
            List<SseEmitter> emitters = emitterRepository.findById(receiverId);
            if (emitters != null) {
                for (SseEmitter emitter : emitters) {
                    sendToUser(receiverId, emitter, eventId, eventName, data);
                }
            }
        }
    }

    public void broadcast(String eventName, Object data) {
        UUID eventId = UUID.randomUUID();
        sseMessageRepository.save(eventId, new SseMessage(eventId, eventName, data));

        emitterRepository.findAll().forEach((receivedId, emitters) -> {
            for (SseEmitter emitter : emitters) {
                sendToUser(receivedId, emitter, eventId, eventName, data);
            }
        });
    }

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void cleanUp() {

        emitterRepository.findAll().forEach((receiverId, emitters) -> {
            List<SseEmitter> copyEmitters = List.copyOf(emitters);
            for (SseEmitter emitter : copyEmitters) {
                boolean isConnected = ping(emitter);
                if (!isConnected) {
                    emitterRepository.delete(receiverId, emitter);
                    log.info("비활성 SSE 연결 정리 - 사용자: {}", receiverId);
                }
            }
        });
    }

    private boolean ping(SseEmitter emitter) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("ping")
                            .data("connected")
            );
            log.debug("ping 전송 성공");
            return true;
        } catch (IOException e) {
            log.error("ping 전송 실패", e);
            emitter.completeWithError(e);
            return false;
        }
    }

    public void sendToUser(UUID receiverId, SseEmitter emitter,
                           UUID eventId, String eventName, Object data) {

        if (emitter == null) {
            log.warn("SSE 연결을 찾을 수 없음 - 사용자: {}", receiverId);
            return;
        }

        try {
            emitter.send(
                    SseEmitter.event()
                            .id(eventId.toString())
                            .name(eventName)
                            .data(data)
            );
            log.info("알림 전송 성공 - 사용자: {}, 이벤트: {}", receiverId, eventName);
        } catch (IOException e) {
            log.error("알림 전송 실패 - 사용자: {}, 이벤트: {}", receiverId, eventName, e);
            emitterRepository.delete(receiverId, emitter);
            emitter.completeWithError(e);
        }
    }
}
