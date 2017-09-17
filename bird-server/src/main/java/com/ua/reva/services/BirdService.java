package com.ua.reva.services;

import com.ua.reva.model.Bird;
import com.ua.reva.model.Sighting;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Bird Service
 */
@Component
public interface BirdService {

    /**
     * Add bird
     * @param bird
     * @return added Bird
     */
    Bird addBird(Bird bird);

    /**
     * Add sighting
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
     * @return
     * @param birdName name of th bird that is regular expression
     * @param startDate Date
     * @param endDate Date
     */
    List<Sighting> listSightings(String birdName, Date startDate, Date endDate);
}
