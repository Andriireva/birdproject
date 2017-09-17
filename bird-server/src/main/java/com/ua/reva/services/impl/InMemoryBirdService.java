package com.ua.reva.services.impl;

import com.ua.reva.datastore.InMemoryData;
import com.ua.reva.exceptions.BirdNotFoundException;
import com.ua.reva.model.Bird;
import com.ua.reva.model.Sighting;
import com.ua.reva.services.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link BirdService}
 */
@Service
public class InMemoryBirdService implements BirdService {

    private InMemoryData inMemoryData;

    @Autowired
    public void setInMemoryData(InMemoryData inMemoryData) {
        this.inMemoryData = inMemoryData;
    }

    public Bird addBird(Bird bird) {
        return inMemoryData.add(bird);
    }

    public Sighting addSighting(Sighting sighting) {
        return inMemoryData.add(sighting);
    }

    public List<Bird> listBirds() {
        return inMemoryData.getBirds();
    }

    public List<Sighting> listSightings(String birdName, Date startDate, Date endDate) {
        return inMemoryData.getSightings().stream().filter(s -> s.getBirdName().matches(birdName)
                && s.getDateTime().toInstant().isAfter(startDate.toInstant())
                && s.getDateTime().toInstant().isBefore(endDate.toInstant())
        ).collect(Collectors.toList());
    }


    public void deleteBird(String name) {
        inMemoryData.removeBird(name);
    }
}
