package com.ua.reva.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Simple class represent HttpStatusCodeException body
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpErrorResponse {
    @JsonProperty
    private Integer status;
    @JsonProperty
    private String message;

    public HttpErrorResponse() {
    }

    public HttpErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
