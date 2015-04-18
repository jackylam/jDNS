package me.jaks.jdns.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/record")
public class RecordResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "rest is working";
	}
}
