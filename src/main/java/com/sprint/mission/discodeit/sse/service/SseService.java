package com.sprint.mission.discodeit.sse.service;

import com.sprint.mission.discodeit.sse.model.SseMessage;
import com.sprint.mission.discodeit.sse.repository.SseEmitterRepository;
import com.sprint.mission.discodeit.sse.repository.SseMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseService {

    private static final long TIMEOUT = 1000L * 60 * 60;

    private final SseEmitterRepository emitterRepository;
    private final SseMessageRepository messageRepository;

    public SseEmitter connect(UUID receiverId, UUID lastEventId) {

        log.info("receiverId={}, lastEventId={}", receiverId, lastEventId);

        SseEmitter emitter = new SseEmitter(TIMEOUT);

        emitterRepository.save(receiverId, emitter);

        emitter.onCompletion(() -> {
            emitterRepository.remove(receiverId, emitter);
            log.info("SSE 연결 종료 - 사용자: {}", receiverId);
        });
        emitter.onTimeout(() -> {
            emitterRepository.remove(receiverId, emitter);
            log.info("SSE 연결 타임 아웃 - 사용자: {}", receiverId);
        });
        emitter.onError((e) -> {
            emitterRepository.remove(receiverId, emitter);
            log.info("SSE 연결 에러 - 사용자: {}, 에러: {}", receiverId, e.getMessage());
        });

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connect")
                            .data("SSE 연결이 수립되었습니다.")
            );
            restoreLostMessages(lastEventId, emitter);
        } catch (IOException e) {
            log.error("초기 메시지 전송 실패 - 사용자: {}", receiverId, e);
            emitterRepository.remove(receiverId, emitter);
            emitter.completeWithError(e);
        }

        return emitter;
    }

    private void restoreLostMessages(UUID lastEventId, SseEmitter emitter) throws IOException {
        List<SseMessage> lostMessages = messageRepository.findAfter(lastEventId);

        for (SseMessage message : lostMessages) {
            emitter.send(
                    SseEmitter.event()
                            .id(message.id().toString())
                            .name(message.eventName())
                            .data(message.data())
            );
        }
    }

    public void send(Collection<UUID> receiverIds, String eventName, Object data) {
        SseMessage message = messageRepository.save(eventName, data);

        for (UUID receiverId : receiverIds) {

            List<SseEmitter> emitters =
                    emitterRepository.findByUserId(receiverId);

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(
                            SseEmitter.event()
                                    .id(message.id().toString())
                                    .name(eventName)
                                    .data(data)
                    );

                } catch (IOException e) {
                    emitterRepository.remove(receiverId, emitter);
                    emitter.completeWithError(e);
                }
            }
        }
    }

    public void broadcast(String eventName, Object data) {
        SseMessage message = messageRepository.save(eventName, data);

        for (Map.Entry<UUID, List<SseEmitter>> entry : emitterRepository.findAll().entrySet()) {
            UUID userId = entry.getKey();

            for (SseEmitter emitter : entry.getValue()) {
                try {
                    emitter.send(
                            SseEmitter.event()
                                    .id(message.id().toString())
                                    .name(eventName)
                                    .data(data)
                    );
                } catch (IOException e) {
                    emitterRepository.remove(userId, emitter);
                    emitter.completeWithError(e);
                }
            }
        }
    }


    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void cleanUp() {
        Map<UUID, List<SseEmitter>> emitters = emitterRepository.findAll();

        for (Map.Entry<UUID, List<SseEmitter>> entry : emitters.entrySet()) {
            UUID userId = entry.getKey();

            for (SseEmitter emitter : entry.getValue()) {
                if (!ping(emitter)) {
                    emitterRepository.remove(userId, emitter);
                }
            }
        }
    }

    private boolean ping(SseEmitter emitter) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("ping")
                            .data("keep-alive")
            );
            return true;
        } catch (IOException e) {
            emitter.completeWithError(e);
            return false;
        }
    }
}
