package me.jaks.jdns.records;

public interface RR {
	
	public String getName();
	public void setName(String name);
    public int getType();
    public void setType(int type);
    public int getRRClass();
    public void setRRClass(int rrclass);
    public long getTtl();
    public void setTtl(long ttl);
    public int getRdLength();
    public void setRdLength(int rdlength);
    public byte[] getRdata();
    public void setRdata(byte[] rdata);
    public boolean getDisabled();
    public void setDisabled(boolean disabled);
    public byte[] serialize();
}
