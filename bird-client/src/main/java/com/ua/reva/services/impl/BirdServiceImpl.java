package com.ua.reva.services.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.reva.cli.exceptions.BirdCliException;
import com.ua.reva.cli.exceptions.UnexpectedBirdCliException;
import com.ua.reva.config.ContextConfigurable;
import com.ua.reva.model.Bird;
import com.ua.reva.model.HttpErrorResponse;
import com.ua.reva.model.Sighting;
import com.ua.reva.services.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

@Component
public class BirdServiceImpl extends ContextConfigurable implements BirdService{

    private RestTemplate restTemplate;

    @Required
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Bird addBird(Bird bird) {
        ResponseEntity<Bird> birdResponseEntity = handleWithErrorHandling(() -> restTemplate.exchange(context.getAddress() + "/api/v1/birds", HttpMethod.PUT, new HttpEntity<Bird>(bird), new ParameterizedTypeReference<Bird>() {}));
        return birdResponseEntity.getBody();
    }

    public Sighting addSighting(Sighting sighting) {
        ResponseEntity<Sighting> sightingResponseEntity = handleWithErrorHandling(() -> restTemplate.exchange(context.getAddress() + "/api/v1/birds/sighting", HttpMethod.PUT, new HttpEntity<Sighting>(sighting), new ParameterizedTypeReference<Sighting>() {}));
        return sightingResponseEntity.getBody();
    }

    public List<Bird> listBirds() {
        ResponseEntity<List<Bird>> exchange = handleWithErrorHandling(() -> restTemplate.exchange(context.getAddress() + "/api/v1/birds", HttpMethod.GET, null, new ParameterizedTypeReference<List<Bird>>() {}));
        return exchange.getBody();
    }

    public void deleteBird(String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("birdName", name);
        handleWithErrorHandling(() -> restTemplate.execute(context.getAddress() + "/api/v1/birds/{birdName}", HttpMethod.DELETE, null, null, params ));
    }

    @Override
    public List<Sighting> listSightings(String birdName, Date startDate, Date endDate) {
        Map<String, Object> requestParams = new LinkedHashMap<>();
        requestParams.put("birdName", birdName);
        requestParams.put("startDate", startDate);
        requestParams.put("endDate", endDate);
        ResponseEntity<List<Sighting>> exchange = handleWithErrorHandling(() -> restTemplate.exchange(context.getAddress() + "/api/v1/birds/sighting", HttpMethod.POST, new HttpEntity<>(requestParams), new ParameterizedTypeReference<List<Sighting>>() {}));
        return exchange.getBody();
    }

    private <R> R handleWithErrorHandling(Supplier<R> restExecutionSupplier) {
        try {
            return restExecutionSupplier.get();
        } catch (HttpStatusCodeException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode()) || HttpStatus.BAD_REQUEST.equals(e.getStatusCode())) {
                try {
                    HttpErrorResponse httpErrorResponse = parse(e.getResponseBodyAsString());
                    throw new BirdCliException(httpErrorResponse.getMessage());
                } catch (IOException e1) {
                    throw new UnexpectedBirdCliException("cannot parse HttpStatusCodeException body", e1);
                }
            } else {
                throw e;
            }
        }
    }

    private HttpErrorResponse parse(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, HttpErrorResponse.class);
    }

}
