package com.ua.reva.controllers;

import com.ua.reva.services.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Management server controller
 */
@RestController
@RequestMapping("/api/v1/management")
public class ManagementServerController {

    private ManagementService managementService;

    @Autowired
    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }

    @PostMapping("/shutdown")
    public void shutdown() {
        managementService.shutDown();
    }
}
