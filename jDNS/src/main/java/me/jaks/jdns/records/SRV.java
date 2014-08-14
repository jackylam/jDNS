package me.jaks.jdns.records;

public interface SRV extends RR {
	
	public int getPriority();
	public void setPriority(int priority);
	public int getWeight();
	public void setWeight(int weight);
	public int getPort();
	public void setPort(int port);
	public String getTarget();
	public void setTarget(String target);
	
}
