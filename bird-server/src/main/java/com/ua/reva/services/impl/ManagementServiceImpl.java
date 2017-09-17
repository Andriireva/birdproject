package com.ua.reva.services.impl;

import com.ua.reva.datastore.DataPersist;
import com.ua.reva.datastore.ScheduleManager;
import com.ua.reva.executor.ParallelTaskRunner;
import com.ua.reva.services.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Rude Implementation of the {@link ManagementService}
 */
public class ManagementServiceImpl implements ManagementService {

    private ParallelTaskRunner taskRunner;
    private List<DataPersist> persistList;
    private List<ScheduleManager> scheduleManagers;

    public ManagementServiceImpl(ParallelTaskRunner taskRunner, List<DataPersist> persistList, List<ScheduleManager> scheduleManagers) {
        this.taskRunner = taskRunner;
        this.persistList = persistList;
        this.scheduleManagers = scheduleManagers;
    }

    @Override
    public void shutDown() {
        //1. Stop auto persisting
        scheduleManagers.forEach(ScheduleManager::cancel);
        //2. Persist data
        persistList.forEach(DataPersist::persistData);
        //3. Stop incoming request
        taskRunner.stopProcess();
        //4 Shutdown the server application
        System.exit(0);

    }
}
