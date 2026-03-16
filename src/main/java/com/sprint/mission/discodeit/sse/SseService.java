package com.sprint.mission.discodeit.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SseService {

    private static final long SSE_TIMEOUT = Long.MAX_VALUE;

    private final SseEmitterRepository sseEmitterRepository;
    private final SseMessageRepository sseMessageRepository;

    public SseEmitter connect(UUID receiverId, UUID lastEventId) {
        SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);

        sseEmitterRepository.save(receiverId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitterRepository.delete(receiverId, sseEmitter));
        sseEmitter.onTimeout(() -> sseEmitterRepository.delete(receiverId, sseEmitter));
        sseEmitter.onError((e) -> sseEmitterRepository.delete(receiverId, sseEmitter));

        boolean connected = ping(sseEmitter);
        if (!connected) {
            sseEmitterRepository.delete(receiverId, sseEmitter);
            return sseEmitter;
        }

        if (lastEventId != null) {
            List<SseMessage> missedMessages = sseMessageRepository.findAllAfter(lastEventId);

            for (SseMessage message : missedMessages) {
                try {
                    sseEmitter.send(
                            SseEmitter.event()
                                    .id(message.id().toString())
                                    .name(message.eventName())
                                    .data(message.data())
                    );
                } catch (IOException e) {
                    sseEmitterRepository.delete(receiverId, sseEmitter);
                    break;
                }
            }
        }

        return sseEmitter;
    }

    public void send(Collection<UUID> receiverIds, String eventName, Object data) {
        SseMessage message = sseMessageRepository.save(eventName, data);

        for (UUID receiverId : receiverIds) {
            List<SseEmitter> sseEmitters = sseEmitterRepository.findAllByReceiverId(receiverId);

            for (SseEmitter sseEmitter : sseEmitters) {
                try {
                    sseEmitter.send(
                            SseEmitter.event()
                                    .id(message.id().toString())
                                    .name(message.eventName())
                                    .data(message.data())
                    );
                } catch (IOException e) {
                    sseEmitterRepository.delete(receiverId, sseEmitter);
                }
            }
        }
    }

    public void broadcast(String eventName, Object data) {
        SseMessage message = sseMessageRepository.save(eventName, data);

        for (Map.Entry<UUID, List<SseEmitter>> entry : sseEmitterRepository.findAll().entrySet()) {
            UUID receiverId = entry.getKey();
            List<SseEmitter> sseEmitters = new ArrayList<>(entry.getValue());

            for (SseEmitter sseEmitter : sseEmitters) {
                try {
                    sseEmitter.send(
                            SseEmitter.event()
                                    .id(message.id().toString())
                                    .name(message.eventName())
                                    .data(message.data())
                    );
                } catch (IOException e) {
                    sseEmitterRepository.delete(receiverId, sseEmitter);
                }
            }
        }
    }

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void cleanUp() {
        for (Map.Entry<UUID, List<SseEmitter>> entry : sseEmitterRepository.findAll().entrySet()) {
            UUID receiverId = entry.getKey();
            List<SseEmitter> sseEmitters = new ArrayList<>(entry.getValue());

            for (SseEmitter sseEmitter : sseEmitters) {
                boolean alive = ping(sseEmitter);
                if (!alive) {
                    sseEmitterRepository.delete(receiverId, sseEmitter);
                }
            }
        }
    }

    private boolean ping(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .name("ping")
                            .data("ping")
            );
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}