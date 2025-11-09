package com.sprint.mission.discodeit.transactional;

import com.sprint.mission.discodeit.transactional.snapshot.Rollbackable;

import java.util.HashMap;
import java.util.Map;

//트랜잭션 실행 시, backupStatus 에 Repository 들을 저장하여, 관리한다.
public class TransactionContext {
    private final Map<String, Rollbackable> backupStates = new HashMap<>();

    // Repository 들이 백업본을 등록할 수 있도록 함
    public void registerBackup(String key, Rollbackable snapshot) {
        backupStates.put(key, snapshot);
    }

    public Rollbackable getBackup(String key) {
        return backupStates.get(key);
    }

    public void commit() {
        backupStates.clear();
    }

    public void rollback() {
        backupStates.forEach( (key, r) -> r.restore());
    }
}
