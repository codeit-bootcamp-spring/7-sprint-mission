package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.SseEmitterRepository;
import com.sprint.mission.discodeit.service.SseService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

@Service
public class BasicSseService implements SseService {
    private final SseEmitterRepository sseEmitterRepository;

    public BasicSseService(SseEmitterRepository sseEmitterRepository) {
        this.sseEmitterRepository = sseEmitterRepository;
    }

    @Override
    @Transactional
    public SseEmitter connect(UUID receiverId, UUID lastEventId) {

        SseEmitter emitter = new SseEmitter();

        sseEmitterRepository.save(receiverId, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.delete(receiverId, emitter));
        emitter.onTimeout(() -> sseEmitterRepository.delete(receiverId, emitter));
        emitter.onError(t -> sseEmitterRepository.delete(receiverId, emitter));

        // 최초 연결 확인용 더미 이벤트
        ping(emitter);

        return emitter;
    }

    @Override
    public void send(Collection<UUID> receiverIds, String eventName, Object data) {
        receiverIds.forEach(userId -> {
            sseEmitterRepository.findByUserId(userId)
                    .forEach(sseEmitter -> {
                        try {
                            sseEmitter.send(
                                    SseEmitter.event()
                                            .name(eventName)
                                            .data(data)
                            );
                        } catch (IOException e) {
                            sseEmitter.complete();
                            sseEmitterRepository.delete(userId, sseEmitter);
                        }
                    });
        });
    }

    @Override
    public void broadcast(String eventName, Object data) {
        ConcurrentMap<UUID, List<SseEmitter>> allEmitters = sseEmitterRepository.findAll();

        allEmitters.forEach((userId, emitters) -> {

                    emitters.forEach(sseEmitter -> {
                        try {
                            sseEmitter.send(
                                    SseEmitter.event()
                                            .name(eventName)
                                            .data(data)
                            );
                        } catch (IOException e) {
                            sseEmitter.complete();
                            sseEmitterRepository.delete(userId, sseEmitter);
                        }
                    });

                }
        );
    }

    @Override
    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void cleanUp() {
        sseEmitterRepository.findAll().forEach((userId, emitters) -> {
            emitters.forEach(emitter -> {
                boolean alive = ping(emitter);
                if (!alive) {
                    sseEmitterRepository.delete(userId, emitter);
                }
            });
        });
    }

    @Override
    public boolean ping(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .name("ping")
                            .data("ping")
            );
            return true;
        } catch (IOException e) {
            sseEmitter.complete();
            return false;
        }
    }
}
