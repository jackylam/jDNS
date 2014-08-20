package me.jaks.jdns.console;

import javax.sql.DataSource;

public interface RecordDaoInt {
	public void createRecord(Record record);
	public Record getRecord(int id);
	public Record[] getRecords(String zone);
	public void updateRecord(Record record);
	public void deleteRecord(int id);
	
}
