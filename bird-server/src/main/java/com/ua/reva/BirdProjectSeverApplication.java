package com.ua.reva;

import com.ua.reva.datastore.*;
import com.ua.reva.executor.ParallelTaskRunner;
import com.ua.reva.model.Bird;
import com.ua.reva.model.Sighting;
import com.ua.reva.services.ManagementService;
import com.ua.reva.services.impl.ManagementServiceImpl;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Executor;


@SpringBootApplication
public class BirdProjectSeverApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(BirdProjectSeverApplication.class);

    private static final String DEFAULT_SUB_DATASTORE_FOLDER = "serverdata";
    private static final String DEFAULT_SUB_DATASTORE_FOLDER_BIRDS_FILE = "birds.txt";
    private static final String DEFAULT_SUB_DATASTORE_FOLDER_SIGHTING_FILE = "sighting.txt";

    private static final String DEFAULT_PROC_COUNT = "2";

    private static final String DEFAULT_PORT = "3000";


    public static void main(String[] args) throws Exception {
        //1. parse incoming arguments
        Option portOption = new Option(null, "port", true, "port of server");
        portOption.setArgs(1);
        portOption.setOptionalArg(true);
        Option dataOption = new Option(null, "data", true, "data store folder");
        dataOption.setArgs(1);
        dataOption.setOptionalArg(true);
        Option procCountOption = new Option(null, "proc_count", true, "positive integer");
        procCountOption.setArgs(1);
        procCountOption.setOptionalArg(true);
        Options options = new Options();
        options.addOption(portOption).addOption(dataOption)
               .addOption(procCountOption);
        CommandLineParser cmdLinePosixParser = new DefaultParser();
        CommandLine commandLine = cmdLinePosixParser.parse(options, args);

        //2. Check that args contain "port" arg. If so override default port.
        String port = "";
        if (commandLine.hasOption("port")) {
            port = commandLine.getOptionValue("port");
        } else {
            port = DEFAULT_PORT;
        }

        //3. Check that args contain "data" arg
        String dataDirectory = "";
        if (commandLine.hasOption("data")) {
            dataDirectory = commandLine.getOptionValue("data");
        } else {
            dataDirectory = FileUtils.getUserDirectoryPath() + File.separator + DEFAULT_SUB_DATASTORE_FOLDER;
        }

        //4. Check is directory exists if not exist it is created if no check write permission to directory
        createIfNeededAndCheckWriteable(dataDirectory);

        //5. Check that args contain "proc_count" arg
        String procCount = "";
        if (commandLine.hasOption("proc_count")) {
            procCount = commandLine.getOptionValue("proc_count");
        } else {
            procCount = DEFAULT_PROC_COUNT;
        }

        //6. forward args for spring boot runner
        String[] bootArgs = new String[3];
        bootArgs[0] = "--server.port=" + port;
        bootArgs[1] = "--server.data=" + dataDirectory;
        bootArgs[2] = "--proc.count=" + procCount;

        SpringApplication.run(BirdProjectSeverApplication.class, bootArgs);
    }

    private static void createIfNeededAndCheckWriteable(String dataDirectory) throws IOException {
        Path directoryPath = Paths.get(dataDirectory);
        if (Files.notExists(directoryPath)) {
            new File(dataDirectory).mkdir();
        } else if (!Files.isWritable(directoryPath)) {
            throw new IOException("Cannot write to folder " + dataDirectory);
        }
    }

    public void run(ApplicationArguments args) throws Exception {
        logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
    }

    @Bean
    public Executor threadPoolTaskExecutor(@Value("${proc.count}") Integer procCount) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(procCount);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor(@Qualifier("threadPoolTaskExecutor") Executor executor) {
        return new ConcurrentTaskExecutor(executor);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public PersistFileSystemDataStore<Bird> birdFileSystemDataStore(TaskScheduler taskScheduler,
                                                                    InMemoryData inMemoryData,
                                                                    @Value("${server.data}") String data) {
        return new PersistFileSystemDataStore<Bird>(taskScheduler, inMemoryData::getBirds, data + File.separator + DEFAULT_SUB_DATASTORE_FOLDER_BIRDS_FILE);
    }

    @Bean
    public PersistFileSystemDataStore<Sighting> sightingFileSystemDataStore(TaskScheduler taskScheduler,
                                                                            InMemoryData inMemoryData,
                                                                            @Value("${server.data}") String data) {
        return new PersistFileSystemDataStore<Sighting>(taskScheduler, inMemoryData::getSightings, data + File.separator + DEFAULT_SUB_DATASTORE_FOLDER_SIGHTING_FILE);
    }

    @Bean
    public DataLoader<Bird> birdDataLoader(@Value("${server.data}") String data) {
        return new LoadFileSystemDataStore<Bird>(data + File.separator + DEFAULT_SUB_DATASTORE_FOLDER_BIRDS_FILE);
    }

    @Bean
    public DataLoader<Sighting> sightingDataLoader(@Value("${server.data}") String data) {
        return new LoadFileSystemDataStore<Sighting>(data + File.separator + DEFAULT_SUB_DATASTORE_FOLDER_SIGHTING_FILE);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public InMemoryData inMemoryData(@Qualifier("birdDataLoader") DataLoader<Bird> birdDataLoader,
                                     @Qualifier("sightingDataLoader") DataLoader<Sighting> sightingDataLoader,
                                     ParallelTaskRunner parallelTaskRunner) {
        return new InMemoryData(birdDataLoader.loadData(Bird.class), sightingDataLoader.loadData(Sighting.class), parallelTaskRunner);
    }

    @Bean
    public ManagementService managementService(
            ParallelTaskRunner taskRunner,
            @Qualifier("birdFileSystemDataStore") DataPersist birdDataPersist,
            @Qualifier("sightingFileSystemDataStore") DataPersist sightingDataPersist,
            @Qualifier("birdFileSystemDataStore") ScheduleManager birdScheduleManager,
            @Qualifier("sightingFileSystemDataStore") ScheduleManager sightingScheduleManager

    ) {
        return new ManagementServiceImpl(taskRunner,
                Arrays.asList(birdDataPersist, sightingDataPersist),
                Arrays.asList(birdScheduleManager, sightingScheduleManager));
    }

}
