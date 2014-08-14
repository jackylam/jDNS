package me.jaks.jdns.records;

public interface NAPTR extends RR {

	public int getOrder();
	public void setOrder(int order);
	public int getPreference();
	public void setPreference(int pref);
	public String getFlags();
	public void setFlags(String flags);
	public String getServices();
	public void setServices(String services);
	public String getRegex();
	public void setRegex(String regex);
	public String getReplacement();
	public void setReplacement(String replace);
}
