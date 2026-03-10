package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.sse.model.SseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class SseEmitterRepository {


    private final Map<UUID, List<SseEvent>> eventStore = new ConcurrentHashMap<>();
    private final Map<UUID,SseEvent> eventIndex = new  ConcurrentHashMap<>();

    private static final int MAX_EVENTS_PER_USER = 100;
    private static final int EVENT_RETENTION_MINUTES = 30;
    private static final int CLEAN_DELAY = 1000 * 60 * 30;


    public UUID saveEvent(UUID userId, String eventName, Object data){
        UUID eventId = UUID.randomUUID();
        SseEvent sseEvent = new SseEvent(
                eventId,
                userId,
                eventName,
                data,
                Instant.now()
        );

        eventStore.computeIfAbsent(userId,k -> Collections.synchronizedList(new ArrayList<>())).add(sseEvent);
        eventIndex.put(eventId, sseEvent);

        List<SseEvent> userEvents = eventStore.get(userId);
        if(userEvents.size()>MAX_EVENTS_PER_USER){
            SseEvent oldEvents = userEvents.remove(0);
            eventIndex.remove(oldEvents.eventId());
        }
        return eventId;
    }

    public List<SseEvent> getEventSince(UUID userId, UUID lastEventId){
        List<SseEvent> userEvents = eventStore.get(userId);
        SseEvent lastEvent = eventIndex.get(lastEventId);
        Instant lastEventTime= lastEvent.createdAt();
        log.info("lastEventId : {}",lastEventId);
        log.info("userEvents : {}",userEvents);

        if (userEvents == null || userEvents.isEmpty()) {
            return Collections.emptyList();
        }

        if (lastEventId == null) {
            return new ArrayList<>(userEvents);
        }
        return userEvents.stream()
                .filter(event -> event.createdAt().isAfter(lastEventTime))
                .collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = CLEAN_DELAY)
    public void cleanupOldEvents(){
        Instant cutoffTime = Instant.now().minus(EVENT_RETENTION_MINUTES, ChronoUnit.MINUTES);
        for(Map.Entry<UUID, List<SseEvent>> entry : eventStore.entrySet()){

            List<SseEvent> userEvents = entry.getValue();

            List<SseEvent> removeEvents = userEvents.stream().filter(
                            event -> event.createdAt().isBefore(cutoffTime)
                    )
                    .toList();

            for(SseEvent event : removeEvents){
                userEvents.remove(event);
                eventIndex.remove(event.eventId());
            }

            if (userEvents.isEmpty()) {
                eventStore.remove(entry.getKey());
            }
        }
    }

}
