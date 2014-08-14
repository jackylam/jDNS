package me.jaks.jdns.console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class DomainSummarizer {

	private DataSource ds;
	private Map<String,String> domainMap = new TreeMap<String,String>();
	
	public DomainSummarizer(DataSource ds) {
		
		this.ds = ds;
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
		      conn = ds.getConnection();
		      st = conn.prepareStatement("SELECT domains.id,domains.name,count(domain_id) from records,domains "
		      		+ "where domains.id = records.domain_id "
		      		+ "group by domain_id");
	          rs = st.executeQuery();
	          
	          while(rs.next()) {
	        	  	domainMap.put(rs.getString(2), Integer.toString(rs.getInt(3)));
	          }
	          for (String name: domainMap.keySet()){

	              String key =name.toString();
	              String value = domainMap.get(name).toString();  
	              System.out.println(key + " " + value);  


	  } 


		      
		    }
		    catch(Exception e) {
		        System.out.println( "problem accessing database");
		        
		      }
		finally {
			JdbcHelper.close(rs);
			JdbcHelper.close(st);
			JdbcHelper.close(conn);
		}
	}
	public Map<String,String> getSummary() {
		return this.domainMap;
		
	}
}
