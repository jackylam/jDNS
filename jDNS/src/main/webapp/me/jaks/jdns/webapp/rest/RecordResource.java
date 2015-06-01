package me.jaks.jdns.webapp.rest;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import me.jaks.jdns.webapp.Record;
import me.jaks.jdns.webapp.RecordDao;

import com.google.gson.Gson;
import com.google.inject.Inject;


@Path("/record")
public class RecordResource {

	Logger logger = Logger.getLogger(RecordResource.class);
	
	@Inject
	private RecordDao recordDao;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRecord(Record record) {
		if(logger.isDebugEnabled())
			logger.debug("domainID: " + record.getDomainId() + " name: " + record.getName() + " type: " 
			+ record.getType() + " content: " + record.getContent() + " ttl: " + record.getTtl() 
			+ " priority: " + record.getPriority() + " changedate: " + record.getChangeDate() 
			+ " disabled: " + record.getDisabled() + " ordername: " + record.getOrderName()
			+ " auth: " + record.getAuth());
				
		try {
			recordDao.createRecord(record);
		} catch (SQLException e) {
			return Response.serverError().build();
		}
		return Response.ok().build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{name}")
	public String getRecord(@PathParam("name") String name) {
		
		Record [] results = recordDao.getRecord(name);
		logger.info("number of records " + results.length);
		Gson gson = new Gson();
		String json = gson.toJson(results);
		return json;
	}
	
	@PUT
	@Path("{name}/{property}/{value}")
	public Response updateRecord(@PathParam("name") String name,
							 @PathParam("property") String property,
							 @PathParam("value") String value) {
		
		if(logger.isDebugEnabled())
			logger.debug("UpdateRecord: name = " + name + " property = " + property + " value = " + value);
		
		try {
			recordDao.updateRecord(name, property, value);
		} catch (IllegalArgumentException e) {
			logger.error("",e);
			return Response.notModified().build();
		} catch (SQLException e) {
			logger.error("",e);
			return Response.serverError().build();
		}				
		return Response.ok().build();
	}
	
	@DELETE
	@Path("{name}")
	public void deleteRecord(@PathParam("name") String name) {
		
		if(logger.isDebugEnabled())
			logger.debug("DeleteRecord: name = " + name);
		recordDao.deleteRecord(name);
	}
}
