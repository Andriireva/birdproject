package com.ua.reva.services;

/**
 * Service to manage the server
 */
public interface ManagementService {

    /**
     * Stop incoming requests, finish the remaining tasks, save data and shutdown the application
     */
    void shutDown();
}
