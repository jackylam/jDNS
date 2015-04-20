package me.jaks.jdns.webapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import me.jaks.jdns.webapp.Record;
import com.google.gson.Gson;


@Path("/record")
public class RecordResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecord() {
		Record record = new Record();
		record.setAuth(0);
		record.setChangeDate(1);
		record.setContent("test.123");
		record.setDisabled(0);
		record.setDomainId(3);
		record.setId(4);
		record.setName("jaks.me");
		record.setOrderName("order.com");
		record.setPriority(10);
		record.setTtl(225);
		record.setType("R");
		
		Gson gson = new Gson();
		String json = gson.toJson(record);
		return json;
	}
}
