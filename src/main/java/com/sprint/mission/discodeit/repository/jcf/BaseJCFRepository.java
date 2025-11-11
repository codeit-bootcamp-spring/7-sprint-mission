package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.transactional.TransactionContext;
import com.sprint.mission.discodeit.transactional.TransactionManager;
import com.sprint.mission.discodeit.transactional.snapshot.Rollbackable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseJCFRepository<V> implements Rollbackable {
    // 수정 전 상태를 백업하기 위한 Map
    protected final Map<UUID, V> data = new ConcurrentHashMap<>();
    private Map<UUID, V> snapshot;

    protected void beforeModify(){
        TransactionContext ctx = TransactionManager.getContext();

        //트랜잭션이 실행 중이고, 이미 해당 저장소가 백업된 상태가 아닐 때
        if (ctx != null && snapshot == null) {
            snapshot = new HashMap<>(data); // 현재 상태 백업
            ctx.registerBackup(this.getClass().getSimpleName(), this); // 복원 대상 등록
        }

    }
    
    //되돌리기
    @Override
    public void restore() {
        if (snapshot != null) {
            data.clear();
            data.putAll(snapshot);
        }
    }
}
