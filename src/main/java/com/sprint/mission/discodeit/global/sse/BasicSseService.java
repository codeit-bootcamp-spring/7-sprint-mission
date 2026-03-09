package com.sprint.mission.discodeit.global.sse;

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
@RequiredArgsConstructor
@Slf4j
public class BasicSseService implements SseService {
    private final SseEmitterRepository sseEmitterRepository;
    private final SseMessageRepository sseMessageRepository;

    @Override
    public SseEmitter connect(UUID receiverId, UUID lastEventId) {

        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        sseEmitterRepository.save(receiverId, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.deleteByUserId(receiverId, emitter));
        emitter.onTimeout(() -> sseEmitterRepository.deleteByUserId(receiverId, emitter));
        emitter.onError(e -> sseEmitterRepository.deleteByUserId(receiverId, emitter));

        boolean ping = ping(emitter); // return 자체는 사용 안함.
        log.debug("ping : {}", ping);

        // replay
        if (lastEventId != null) {
            List<SseMessage> missedEvents =
                    sseMessageRepository.findMessagesAfter(receiverId, lastEventId);

            for (SseMessage message : missedEvents) {
                try {
                    emitter.send(
                            SseEmitter.event()
                                    .id(message.getId().toString())
                                    .name(message.getEventName())
                                    .data(message.getData())
                    );
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        }

        return emitter;
    }

    // 특정 사용자들에게 전송
    @Override
    public void send(Collection<UUID> receiverIds, String eventName, Object data) {
        for (UUID receiverId : receiverIds) {
            sendToUser(receiverId, eventName, data);
        }
    }

    // 모든 사용자들에게 전송
    @Override
    public void broadcast(String eventName, Object data) {
        sseEmitterRepository.findAllUserIds().forEach(receiverId -> {
            sendToUser(receiverId, eventName, data);
        });
    }

    @Override
    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void cleanUp() {
        List<SseEmitter> sseEmitters = sseEmitterRepository.findAll();
        for (SseEmitter emitter : sseEmitters) {
            if (!ping(emitter)) {
                sseEmitterRepository.deleteByEmitter(emitter);
                emitter.complete();
                log.debug("만료된 emitter 삭제");
            }
        }
    }

    private boolean ping(SseEmitter emitter) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("ping")
                            .data("alive")
            );
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void sendToUser(UUID receiverId, String eventName, Object data) {
        UUID eventId = sseMessageRepository.save(eventName, receiverId, data);
        List<SseEmitter> emitters = sseEmitterRepository.findAllById(receiverId);

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data)
                        .id(eventId.toString()));
            } catch (IOException e) {
                log.warn("전송 실패 - 사용자: {}, 이벤트: {}", receiverId, eventName);
                sseEmitterRepository.deleteByUserId(receiverId, emitter);
                log.error(e.getMessage(), e);
            }
        }
    }
}
