package com.ua.reva.executor.impl;

import com.ua.reva.exceptions.BirdException;
import com.ua.reva.executor.ParallelTaskRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * Parallel Task Runner Implementation
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ParallelTaskRunnerImpl implements ParallelTaskRunner {

    private final AsyncTaskExecutor taskExecutor;

    /**
     * Field that indicate is new execute allowed.
     * See  {@link ParallelTaskRunnerImpl#stopProcess()}
     */
    private boolean allowNewExecute = true;

    @Autowired
    public ParallelTaskRunnerImpl(AsyncTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void stopProcess() {
        allowNewExecute = false;
    }

    @Override
    public <R> R execute(Supplier<R> supplier) {
        if (allowNewExecute) {
            try {
                return taskExecutor.submit(supplier::get).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException();
            }
        } else {
            throw new BirdException("Service is in shout down process. It is not allowed to process new requests");
        }

    }
}
