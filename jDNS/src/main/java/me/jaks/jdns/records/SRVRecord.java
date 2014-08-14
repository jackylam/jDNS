package me.jaks.jdns.records;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class SRVRecord implements SRV{

	private String name;
	private int type;
	private int rrclass;
	private long ttl;
	private int rdlength;
	private byte[] rdata;
	private int priority;
	private int weight;
	private int port;
	private String target;
	private boolean disabled;
	
	public SRVRecord() {
		type = 33;
		rrclass = 1;
	}
	public SRVRecord(String name,long ttl,int rrclass,int priority,int weight,int port,String target,boolean disabled) {
		
		type = 33;
		this.name = name;
		this.ttl = ttl;
		this.rrclass = rrclass;
		this.priority = priority;
		this.weight = weight;
		this.port = port;
		this.target = target;
		this.disabled = disabled;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] priorityBytes = new byte[2];
		byte[] weightBytes = new byte[2];
		byte[] portBytes = new byte[2];
		
		priorityBytes[0] = (byte)(priority >>> 8);
		priorityBytes[1] = (byte) priority;
		weightBytes[0] = (byte)(weight >>> 8);
		weightBytes[1] = (byte) weight;
		portBytes[0] = (byte)(port >>> 8);
		portBytes[1] = (byte) port;
		
		ByteArrayOutputStream targetStream = new ByteArrayOutputStream();
		String[] targetArray = target.split("\\.");
		for(String label:targetArray) {
			int temp = label.length();
			targetStream.write((byte) temp);
			try {
				targetStream.write(label.getBytes("US-ASCII"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		targetStream.write((byte)0);
		byte[] targetBytes = targetStream.toByteArray();
		try {
			baos.write(priorityBytes);
			baos.write(weightBytes);
			baos.write(portBytes);
			baos.write(targetBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
		setRdata(baos.toByteArray());
		rdlength = baos.size();
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int getRRClass() {
		return rrclass;
	}

	@Override
	public void setRRClass(int rrclass) {
		this.rrclass = rrclass;
	}

	@Override
	public long getTtl() {
		return ttl;
	}

	@Override
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	@Override
	public int getRdLength() {
		return rdlength;
	}

	@Override
	public void setRdLength(int rdlength) {
		this.rdlength = rdlength;
	}

	@Override
	public byte[] getRdata() {
		return rdata;
	}

	@Override
	public void setRdata(byte[] rdata) {
		this.rdata = rdata;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String getTarget() {
		return target;
	}

	@Override
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public boolean getDisabled() {
		return disabled;
	}
	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	@Override
	public byte[] serialize() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] typeBytes = new byte[2];
		byte[] rrclassBytes = new byte[2];
		byte[] ttlBytes = new byte[4];
		byte[] rdlengthBytes = new byte[2];
		
		typeBytes[0] = (byte)(type >>> 8);
		typeBytes[1] = (byte) type;
		rrclassBytes[0] = (byte) (rrclass >>> 8);
		rrclassBytes[1] = (byte) rrclass;
		
		ttlBytes[0] = (byte)(ttl >>> 24);
		ttlBytes[1] = (byte)(ttl >>> 16);
		ttlBytes[2] = (byte) (ttl >>> 8);
		ttlBytes[3] = (byte) ttl;
		
		rdlengthBytes[0] = (byte)(rdlength >>> 8);
		rdlengthBytes[1] = (byte)(rdlength);
		
		try {
			baos.write(typeBytes);
			baos.write(rrclassBytes);
			baos.write(ttlBytes);
			baos.write(rdlengthBytes);
			baos.write(rdata);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
}
