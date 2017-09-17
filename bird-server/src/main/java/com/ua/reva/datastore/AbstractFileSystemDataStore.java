package com.ua.reva.datastore;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract file system data store.
 * This class is abstract represent of 1 data type of data store
 */
public abstract class AbstractFileSystemDataStore<D> {

    /**
     * Jackson Mapper
     */
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * String path to file
     */
    protected final String file;

    public AbstractFileSystemDataStore(String file) {
        this.file = file;

        //Create file that contain data
        Path path = Paths.get(file);
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException("cannot create file: " + file);
            }
        }
    }
}
