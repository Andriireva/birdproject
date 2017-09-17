package com.ua.reva.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

/**
 * Cli context provide class
 */
@Component
public class ContextConfigurable {

    /**
     * Context of the application
     */
    protected Context context;

    @Required
    @Autowired
    public void setContext(Context context) {
        this.context = context;
    }
}
