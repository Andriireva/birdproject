package com.ua.reva.services;

import com.ua.reva.model.Bird;
import com.ua.reva.model.Sighting;

import java.util.Date;
import java.util.List;

/**
 * Service to communicate with Server
 */
public interface BirdService {

    /**
     * Add bird
     * @param bird
     * @return Bird
     */
    Bird addBird(Bird bird);

    /**
     * Add Sighting
     * @param sighting
     * @return
     */
    Sighting addSighting(Sighting sighting);

    /**
     * Listing birds
     * @return collection of the Bird
     */
    List<Bird> listBirds();

    /**
     * Delete bird
     * @param name name of a bird
     */
    void deleteBird(String name);

    /**
     * List sighting
     * @param birdName regular expression for bird name
     * @param startDate start date
     * @param endDate end date
     * @return
     */
    List<Sighting> listSightings(String birdName, Date startDate, Date endDate);
}
