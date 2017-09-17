package com.ua.reva.datastore;

import java.util.List;

/**
 * Data loader of type <D>
 */
public interface DataLoader<D> {

    /**
     * Load data from store
     * @return List of entities
     */
    List<D> loadData(Class<D> clz);
}
