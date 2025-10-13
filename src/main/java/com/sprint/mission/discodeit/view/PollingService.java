package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.DiscodeitApplication;
import com.sprint.mission.discodeit.common.Event;
import com.sprint.mission.discodeit.service.EventService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;
import java.util.UUID;

public class PollingService extends Service<List<Event<?>>> {

    private final EventService eventService;
    private final UUID currentUserId;

    public PollingService(UUID currentUserId) {
        this.currentUserId = currentUserId;
        this.eventService = DiscodeitApplication.getAppConfig().getEventService();
    }

    @Override
    protected Task<List<Event<?>>> createTask() {
        return new Task<>() {
            @Override
            protected List<Event<?>> call() throws Exception {
                // 이 부분이 백그라운드 스레드에서 실행됩니다.
                // 서버에 구독 요청을 보내고, 응답이 올 때까지 기다립니다.
                return eventService.subscribe(currentUserId).join();
            }
        };
    }
}