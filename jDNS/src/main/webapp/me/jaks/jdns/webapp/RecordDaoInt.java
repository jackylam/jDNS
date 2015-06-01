package me.jaks.jdns.webapp;

import java.sql.SQLException;

public interface RecordDaoInt {
	public void createRecord(Record record) throws SQLException;
	public Record[] getRecord(String name);
	public void updateRecord(String name, String property, String value) throws IllegalArgumentException, SQLException;
	public void deleteRecord(String name);
	
}
