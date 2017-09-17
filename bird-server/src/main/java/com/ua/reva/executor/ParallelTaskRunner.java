package com.ua.reva.executor;

import java.util.function.Supplier;

/**
 * Task runner
 */
public interface ParallelTaskRunner {

    /**
     * Execute in task supplier
     * @param supplier business logic provider
     * @param <R> result
     * @return
     */
    <R> R execute(Supplier<R> supplier);

    /**
     * Stop runner
     */
    void stopProcess();
}
