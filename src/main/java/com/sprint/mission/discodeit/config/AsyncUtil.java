//package com.sprint.mission.discodeit.config;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.function.Supplier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.retry.annotation.EnableRetry;
//
//@Configuration
////@EnableRetry
//public class AsyncUtil {
//
//    public <T> CompletableFuture retry(Supplier<CompletableFuture<T>> action, int retryCount) {
//        return action.get()
//            .handle((res,ex) -> {
//                if (ex == null) {
//                    return CompletableFuture.completedFuture(res);
//                }
//
//                if (retryCount > 0) {
//                    return retry(action, retryCount - 1);
//                }
//
//                return CompletableFuture.failedFuture(ex);
//            })
//            .thenCompose(cf -> cf);
//    }
//}
