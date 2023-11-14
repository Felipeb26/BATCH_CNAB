package com.batsworks.batch.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class AsyncFunctions<T, R> {

    public Future<T> object(Function<R, T> function, R r) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(2);
        return service.submit(() -> function.apply(r));
    }
}
