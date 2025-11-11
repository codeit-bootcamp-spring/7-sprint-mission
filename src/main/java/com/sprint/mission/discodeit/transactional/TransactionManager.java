package com.sprint.mission.discodeit.transactional;

//TransactionContext의 backupStatus 가 스레드마다 독립적으로 실행될 수 있도록 ThreadLocal에 TransactionContext 를 넣는다.
public class TransactionManager {
    private static final ThreadLocal<TransactionContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(TransactionContext ctx) {
        CONTEXT.set(ctx);
    }

    public static TransactionContext getContext() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
