package com.ua.reva.services;

/**
 * Service to manage the server
 */
public interface ManagementService {

    /**
     * Send shutdown request to server.
     * Stop incoming requests, finish the remaining tasks and shutdown the application
     */
    void shutDown();
}
