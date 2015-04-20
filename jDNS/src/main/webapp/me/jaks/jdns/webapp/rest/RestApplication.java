package me.jaks.jdns.webapp.rest;

import org.glassfish.jersey.server.ResourceConfig;

//@ApplicationPath("rest")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages("me.jaks.jdns.rest");
    }
}
