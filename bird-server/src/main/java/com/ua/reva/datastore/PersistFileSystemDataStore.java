package com.ua.reva.datastore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Supplier;

/**
 * Scheduler for persist data to the file system
 */
public class PersistFileSystemDataStore<D> extends AbstractFileSystemDataStore<D> implements DataPersist, ScheduleManager{

    private TaskScheduler taskScheduler;

    private Supplier<List<D>> dataProvider;

    private final ObjectMapper mapper = new ObjectMapper();

    private ScheduledFuture<?> schedule;

    public PersistFileSystemDataStore(TaskScheduler taskScheduler, Supplier<List<D>> dataProvider, String file) {
        super(file);
        this.taskScheduler = taskScheduler;
        this.dataProvider = dataProvider;
    }

    @Override
    public void persistData() {
        try {
            //save to folder
            mapper.writeValue(new File(file), dataProvider.get());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Cancel schedule
     */
    @Override
    public void cancel() {
        schedule.cancel(false);
    }

    /**
     * Post construct method that start scheduler to
     * persist data to the file {@link AbstractFileSystemDataStore#file} on the file system
     */
    @PostConstruct
    public void startSchedule() {
        this.schedule = taskScheduler.schedule(this::persistData, new CronTrigger("0/1 * * * * ?"));
    }

}
