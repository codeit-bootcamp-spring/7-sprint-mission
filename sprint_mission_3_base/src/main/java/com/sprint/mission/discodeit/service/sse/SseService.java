package com.sprint.mission.discodeit.service.sse;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {

    private static final long TIMEOUT = 60L * 60L * 1000L;

    private final SseEmitterRepository sseEmitterRepository;
    private final SseMessageRepository sseMessageRepository;

    public SseEmitter connect(UUID receiverId, UUID lastEventId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        sseEmitterRepository.add(receiverId, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.remove(receiverId, emitter));
        emitter.onTimeout(() -> sseEmitterRepository.remove(receiverId, emitter));
        emitter.onError(ex -> sseEmitterRepository.remove(receiverId, emitter));

        ping(emitter);

        List<SseMessage> missedMessages = sseMessageRepository.findMissedMessages(receiverId, lastEventId);
        for (SseMessage message : missedMessages) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .id(message.id().toString())
                                .name(message.eventName())
                                .data(message.data())
                );
            } catch (IOException e) {
                sseEmitterRepository.remove(receiverId, emitter);
                break;
            }
        }

        return emitter;
    }

    public void send(Collection<UUID> receiverIds, String eventName, Object data) {
        for (UUID receiverId : receiverIds) {
            SseMessage message = new SseMessage(
                    UUID.randomUUID(),
                    receiverId,
                    eventName,
                    data,
                    Instant.now()
            );

            sseMessageRepository.save(message);

            List<SseEmitter> emitters = new ArrayList<>(sseEmitterRepository.findAll(receiverId));
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(
                            SseEmitter.event()
                                    .id(message.id().toString())
                                    .name(message.eventName())
                                    .data(message.data())
                    );
                } catch (IOException e) {
                    sseEmitterRepository.remove(receiverId, emitter);
                }
            }
        }
    }

    public void broadcast(String eventName, Object data) {
        List<UUID> receiverIds = new ArrayList<>(sseEmitterRepository.findAll().keySet());
        send(receiverIds, eventName, data);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void cleanUp() {
        for (var entry : sseEmitterRepository.findAll().entrySet()) {
            UUID userId = entry.getKey();
            List<SseEmitter> emitters = new ArrayList<>(entry.getValue());
            for (SseEmitter emitter : emitters) {
                if (!ping(emitter)) {
                    sseEmitterRepository.remove(userId, emitter);
                }
            }
        }
    }

    private boolean ping(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event().name("ping").data("ping"));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}