package com.ua.reva.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Context {

    private static final int DEFAULT_PORT = 3000;
    private static final String DEFAULT_IP_ADDRESS = "localhost";
    private static final String DEFAULT_PROTOCOL = "http";

    /**
     * server port. default 3000.
     */
    private int port = DEFAULT_PORT;

    /**
     * server ip address. default localhost.
     */
    private String ipAddress = DEFAULT_IP_ADDRESS;

    /**
     * server protocol. default http.
     */
    private String protocol = DEFAULT_PROTOCOL;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Value("${bird.server.port}")
    public void setPort(int port) {
        this.port = port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return protocol + "://" + ipAddress + ":" + port;
    }
}
