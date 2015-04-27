package me.jaks.jdns.webapp;

public interface RecordDaoInt {
	public void createRecord(Record record);
	public Record[] getRecord(String name);
	public void updateRecord(Record record);
	public void deleteRecord(String name);
	
}
