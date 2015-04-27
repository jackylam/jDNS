package me.jaks.jdns.webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class RecordDao implements RecordDaoInt {

	private static Logger logger = Logger.getLogger(RecordDao.class);
	private DataSource ds;
	
	public RecordDao() {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			this.ds = (DataSource) ctx.lookup("jdbc/mysql");
		} catch (NamingException e) {
			logger.error("",e);
		}
		
		
	}
	@Override
	public void createRecord(Record record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Record[] getRecord(String name) {
		
		ArrayList<Record> list = new ArrayList<Record>();
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT id,domain_id,name,type,content,ttl,priority,change_date,disabled,ordername,auth FROM records "
					+ "WHERE name = ?";
			
			conn = ds.getConnection();
			st = conn.prepareStatement(sql);
		    st.setString(1, name);
	        rs = st.executeQuery();
	        
	        while(rs.next()) {
	       
	        	Record record = new Record();
	       
	        	record.setId(rs.getInt(1));
	        	record.setDomainId(rs.getInt(2));
	        	record.setName(rs.getString(3));
	        	record.setType(rs.getString(4));
	        	record.setContent(rs.getString(5));
	        	record.setTtl(rs.getInt(6));
	        	record.setPriority(rs.getInt(7));
	        	record.setChangeDate(rs.getInt(8));
	        	record.setDisabled(rs.getInt(9));
	        	record.setOrderName(rs.getString(10));
	        	record.setAuth(rs.getInt(11));
	    
	        	list.add(record);
	        }
		}
		catch(SQLException e) {
			logger.error( "", e );
		}
		finally {
			JdbcHelper.close(rs);
			JdbcHelper.close(st);
			JdbcHelper.close(conn);
		}
		if(logger.isDebugEnabled())
			logger.debug("RecordDao getRecord: number of item in arraylist is " + list.size());
		return list.toArray(new Record[list.size()]);
	}

	@Override
	public void updateRecord(Record record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRecord(String name) {
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(logger.isDebugEnabled())
			logger.debug("Deleting " + name);
		
		try {
			String sql = "DELETE from records WHERE name = ?";
			conn = ds.getConnection();
			st = conn.prepareStatement(sql);
			st.setString(1, name);
			st.executeUpdate();
			
		} catch (SQLException e) {
			logger.error("",e);
		}
		finally {
			JdbcHelper.close(rs);
			JdbcHelper.close(st);
			JdbcHelper.close(conn);
		}
		
		

	}

}
