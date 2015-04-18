package me.jaks.jdns.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

//@ApplicationPath("rest")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages("me.jaks.jdns.rest");
    }
}
