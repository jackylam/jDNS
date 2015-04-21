package me.jaks.jdns.webapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import me.jaks.jdns.webapp.Record;
import me.jaks.jdns.webapp.RecordDao;

import com.google.gson.Gson;
import com.google.inject.Inject;


@Path("/record/{name}")
public class RecordResource {

	Logger logger = Logger.getLogger("jDNS");
	
	@Inject
	private RecordDao recordDao;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecord(@PathParam("name") String name) {
		
		Record [] results = recordDao.getRecord(name);
		logger.info("number of records " + results.length);
		Gson gson = new Gson();
		String json = gson.toJson(results);
		return json;
	}
}
