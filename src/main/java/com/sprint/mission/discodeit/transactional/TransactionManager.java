package com.sprint.mission.discodeit.transactional;

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
