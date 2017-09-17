package com.ua.reva.datastore;

import com.ua.reva.exceptions.BirdAlreadyExistException;
import com.ua.reva.exceptions.BirdNotFoundException;
import com.ua.reva.executor.ParallelTaskRunner;
import com.ua.reva.model.Bird;
import com.ua.reva.model.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * In memory data store that stores Birds and Sighting
 */
public class InMemoryData {

    /**
     * Synchronized List of Birds
     */
    private final List<Bird> birds;

    /**
     * Synchronized List of Sightings
     */
    private final List<Sighting> sightings;

    private ParallelTaskRunner parallelTaskRunner;

    public InMemoryData(List<Bird> birds, List<Sighting> sightings, ParallelTaskRunner parallelTaskRunner) {
        this.birds = birds;
        this.sightings = sightings;
        this.parallelTaskRunner = parallelTaskRunner;
    }

    /**
     * Return view of birds
     *
     * @return List<Bird>
     */
    public List<Bird> getBirds() {
        return parallelTaskRunner.execute(() -> birds);
    }

    /**
     * Returns view of Sighting
     *
     * @return List<Sighting>
     */
    public List<Sighting> getSightings() {
        return parallelTaskRunner.execute(() -> sightings);
    }

    /**
     * Add Bird
     *
     * @param bird
     * @return
     */
    public Bird add(Bird bird) {
        if (parallelTaskRunner.execute(() -> birds).stream().noneMatch(b -> b.getName().equals(bird.getName()))) {
            parallelTaskRunner.execute(() -> birds.add(bird));
        } else {
            throw new BirdAlreadyExistException("Bird with name " + bird.getName() + " already exist");
        }

        return bird;
    }

    /**
     * Add Sighting
     *
     * @param sighting
     * @return
     */
    public Sighting add(Sighting sighting) {
        parallelTaskRunner.execute(() -> sightings.add(sighting));
        return sighting;
    }

    /**
     * Remove Bird by name
     *
     * @param name
     */
    public void removeBird(String name) {
        Bird bird = parallelTaskRunner.execute(() -> birds).stream()
                .filter(b -> b.getName().equals(name))
                .findFirst().orElse(null);
        if (bird != null) {
            parallelTaskRunner.execute(() -> birds.remove(bird));
        } else {
            throw new BirdNotFoundException("Bird with name " + name + " does not exist");
        }
    }
}
