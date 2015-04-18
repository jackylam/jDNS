package me.jaks.jdns.console;

public interface RecordDaoInt {
	public void createRecord(Record record);
	public Record getRecord(String name);
	public Record[] getRecords(String zone);
	public void updateRecord(Record record);
	public void deleteRecord(String name);
	
}
