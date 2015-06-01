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
	public void createRecord(Record record) throws SQLException {
		
		Connection conn = null;
		PreparedStatement st = null;
		boolean status = false;

		String sql = "INSERT INTO records (domain_id,name,type,content,ttl,priority,change_date,disabled,ordername,auth) values (?,?,?,?,?,?,?,?,?,?)";
		
		try {
			conn = ds.getConnection();
			st = conn.prepareStatement(sql);
		    st.setInt(1, record.getDomainId());
		    st.setString(2, record.getName());
		    st.setString(3,record.getType());
		    st.setString(4, record.getContent());
		    st.setInt(5, record.getTtl());
		    st.setInt(6, record.getPriority());
		    st.setInt(7, record.getChangeDate());
		    st.setInt(8, record.getDisabled());
		    st.setString(9, record.getOrderName());
		    st.setInt(10, record.getAuth());
	        st.executeUpdate();
	        
		} catch (SQLException e) {
			if(logger.isDebugEnabled())
				logger.error("",e);
			status = true;
		}
		finally {
			JdbcHelper.close(st);
			JdbcHelper.close(conn);
			if (status==true)
				throw new SQLException("SQL failed");
		}
		
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
	public void updateRecord(String name, String property, String value) throws IllegalArgumentException, SQLException {
		
		String sql;
		String columnType;
		boolean status = false;
		
		switch (property) {
		case "domainid":
			sql = "UPDATE records SET domainid = ? WHERE name = ?";
			columnType = "integer";
			break;
		case "type":
			sql = "UPDATE records SET type = ? WHERE name = ?";
			columnType = "string";
			break;
		case "content":
			sql = "UPDATE records SET content = ? WHERE name = ?";
			columnType = "string";
			break;
		case "ttl":
			sql = "UPDATE records SET ttl = ? WHERE name = ?";
			columnType = "integer";
			break;
		case "priority":
			sql = "UPDATE records SET priority = ? WHERE name = ?";
			columnType = "integer";
			break;
		case "changedate":
			sql = "UPDATE records SET changedate = ? WHERE name = ?";
			columnType = "integer";
			break;
		case "disabled":
			sql = "UPDATE records SET disabled = ? WHERE name = ?";
			columnType = "integer";
			break;
		case "ordername":
			sql = "UPDATE records SET ordername = ? WHERE name = ?";
			columnType = "string";
			break;
		case "auth":
			sql = "UPDATE records SET auth = ? WHERE name = ?";
			columnType = "integer";
			break;
		default:
			throw new IllegalArgumentException("Invalid property name");
		}
		
		Connection conn = null;
		PreparedStatement st = null;
		try {	
			conn = ds.getConnection();
			st = conn.prepareStatement(sql);
			
			if(columnType.equals("string"))
				st.setString(1, value);
			else
				st.setInt(1, Integer.parseInt(value));
			
			st.setString(2,name);
			st.executeUpdate();
		} catch(SQLException e) {
			logger.error("",e);
			status = true;
			
		} finally {
			JdbcHelper.close(st);
			JdbcHelper.close(conn);
			if (status==true)
				throw new SQLException("SQL failed");
		}
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
