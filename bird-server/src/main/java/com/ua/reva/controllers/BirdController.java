package com.ua.reva.controllers;

import com.ua.reva.exceptions.BirdException;
import com.ua.reva.model.Bird;
import com.ua.reva.model.HttpErrorResponse;
import com.ua.reva.model.Sighting;
import com.ua.reva.services.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Bird Controller
 */
@RestController
@RequestMapping("/api/v1/birds")
public class BirdController {

    private BirdService birdService;

    @Autowired
    public void setBirdService(BirdService birdService) {
        this.birdService = birdService;
    }

    @PutMapping
    public HttpEntity<Bird> addBird(@RequestBody Bird bird) {
        return new HttpEntity<Bird>(birdService.addBird(bird));
    }

    @PutMapping("/sighting")
    public HttpEntity<Sighting> addSighting(@RequestBody Sighting sighting) {
        return new HttpEntity<Sighting>(birdService.addSighting(sighting));
    }

    @GetMapping
    public HttpEntity<List<Bird>> listBirds() {
        return new HttpEntity<List<Bird>>(birdService.listBirds());
    }

    @PostMapping("/sighting")
    public HttpEntity<List<Sighting>> listSighting(@RequestBody Map params) {
        return new HttpEntity<List<Sighting>>(birdService.listSightings(String.valueOf(params.get("birdName")), new Date((Long) params.get("startDate")), new Date((Long) params.get("endDate"))));
    }

    @DeleteMapping("/{birdName}")
    public void deleteBird(@PathVariable("birdName") String name) {
        birdService.deleteBird(name);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BirdException.class)
    @ResponseBody
    public HttpErrorResponse handleBadRequest(HttpServletRequest req, Exception ex) {
        return new HttpErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}
