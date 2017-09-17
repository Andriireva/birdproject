package com.ua.reva.services.impl;

import com.ua.reva.config.ContextConfigurable;
import com.ua.reva.services.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of the {@link com.ua.reva.services.ManagementService}
 */
@Component
public class ManagementServiceImpl extends ContextConfigurable implements ManagementService {

    private RestTemplate restTemplate;

    @Required
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Shutdown server
     */
    @Override
    public void shutDown() {
        restTemplate.postForLocation(context.getAddress() + "/api/v1/management/shutdown", null);
    }
}
