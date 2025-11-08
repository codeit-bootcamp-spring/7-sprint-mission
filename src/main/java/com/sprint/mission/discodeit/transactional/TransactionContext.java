package com.sprint.mission.discodeit.transactional;

import com.sprint.mission.discodeit.transactional.snapshot.Rollbackable;

import java.util.HashMap;
import java.util.Map;

//backup을 하고 backup 내용을 가져오고
// commit :
public class TransactionContext {
    private final Map<String, Object> backupStates = new HashMap<>();

    // Repository 들이 백업본을 등록할 수 있도록 함
    public void registerBackup(String key, Object snapshot) {
        backupStates.put(key, snapshot);
    }

    public Object getBackup(String key) {
        return backupStates.get(key);
    }

    public void commit() {
        backupStates.clear();
    }

    public void rollback() {
        backupStates.forEach((key, snapshot) -> {
            if (snapshot instanceof Rollbackable r) {
                r.restore();
            }
        });
    }
}
