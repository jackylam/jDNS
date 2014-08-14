package me.jaks.jdns.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import me.jaks.jdns.records.ARecord;
import me.jaks.jdns.records.NAPTRRecord;
import me.jaks.jdns.records.SRVRecord;

import org.apache.log4j.Logger;

public class RecordRepo {
	private static final Logger logger = Logger.getLogger(RecordRepo.class);

	public static ARecord[] getARecord(String name, DataSource ds) {
		
		boolean found = false;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<ARecord> aList = new ArrayList<ARecord>();
		
		try {
			conn = ds.getConnection();
			st = conn.prepareStatement("SELECT ttl,content,disabled from records where name = ? and type = ?");
			st.setString(1, name);
			st.setString(2, "a");
			rs = st.executeQuery();
			while(rs.next()) {
				long ttl = rs.getLong(1);
				String ipaddress = rs.getString(2);
				boolean disabled = rs.getBoolean(3);
				ARecord temp = new ARecord(name,ttl,ipaddress,disabled);
				found = true;
				aList.add(temp);
			}
			st.close();
			rs.close();
			
		} catch (SQLException e) {
			logger.error("",e);
		}
		finally {
			JDBChelper.close(rs);
			JDBChelper.close(st);
			JDBChelper.close(conn);
			
		}
			if(found == true)
				return aList.toArray(new ARecord[aList.size()]);
			else
				return null;
	}
	
	public static SRVRecord[] getSRVRecord(String name, DataSource ds) {
			
			boolean found = false;
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			ArrayList<SRVRecord> aList = new ArrayList<SRVRecord>();
			
			try {
				conn = ds.getConnection();
				st = conn.prepareStatement("SELECT ttl,priority,content,disabled from records where name = ? and type = ?");
				st.setString(1, name);
				st.setString(2,"srv");
				rs = st.executeQuery();
				while(rs.next()) {
					long ttl = rs.getLong(1);
					int rrclass = 1;
					int priority = rs.getInt(2);
					String content = rs.getString(3);
					boolean disabled = rs.getBoolean(4);
					String[] data = content.split("[ ]+");
					int weight = Integer.parseInt(data[0]);
					int port = Integer.parseInt(data[1]);
					String target = data[2];
					SRVRecord temp = new SRVRecord(name,ttl,rrclass,priority,weight,port,target,disabled);
					found = true;
					aList.add(temp);
				}
				st.close();
				rs.close();
				
			} catch (SQLException e) {
				logger.error("",e);
			}
			finally {
				JDBChelper.close(rs);
				JDBChelper.close(st);
				JDBChelper.close(conn);
				
			}
				if(found == true)
					return aList.toArray(new SRVRecord[aList.size()]);
				else
					return null;
		}
	
	public static NAPTRRecord[] getNAPTRRecord(String name, DataSource ds) {
		
		boolean found = false;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<NAPTRRecord> aList = new ArrayList<NAPTRRecord>();
		
		try {
			conn = ds.getConnection();
			
			
			st = conn.prepareStatement("SELECT ttl,content,disabled from records where name = ? and type = ?");
			st.setString(1, name);
			st.setString(2,"naptr");
			rs = st.executeQuery();
	
			while(rs.next()) {
				long ttl = rs.getLong(1);
				int rrclass = 1;
				String content = rs.getString(2);
				boolean disabled = rs.getBoolean(3);
				String[] data = content.split("[ ]+");
				int order = Integer.parseInt(data[0]);
				int pref = Integer.parseInt(data[1]);
				String flags = data[2].replaceAll("\"", "");
				String services = data[3].replaceAll("\"", "");
				String regex = data[4].replaceAll("\"","");
				String replace = data[5];
				NAPTRRecord temp = new NAPTRRecord(name,ttl,rrclass,order,pref,flags,services,regex,replace,disabled);
				found = true;
				aList.add(temp);
			}
			st.close();
			rs.close();
			
		} catch (SQLException e) {
			logger.error("",e);
		}
		finally {
			JDBChelper.close(rs);
			JDBChelper.close(st);
			JDBChelper.close(conn);
			
		}
			if(found == true)
				return aList.toArray(new NAPTRRecord[aList.size()]);
			else
				return null;
	}
}
