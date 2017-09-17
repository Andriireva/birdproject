package com.ua.reva.datastore;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {@link DataLoader} that load data from file
 */
public class LoadFileSystemDataStore<D> extends AbstractFileSystemDataStore<D> implements DataLoader<D> {


    public LoadFileSystemDataStore(String file) {
        super(file);
    }

    @Override
    public List<D> loadData(Class<D> clz) {
        try {
            CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(List.class, clz);
            return MAPPER.readValue(new File(file), collectionType);
        } catch (FileNotFoundException | JsonMappingException e) {
            return new LinkedList<D>();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

}
