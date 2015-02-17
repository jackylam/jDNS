package me.jaks.jdns.records;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CNAMERecord implements CNAME {

	private String name;
	private int type;
	private int rrclass;
	private long ttl;
	private int rdlength;
	private byte[] rdata;
	private boolean disabled;
	
	public CNAMERecord() {
		type = 5;
		rrclass = 1;
	}
	
	public CNAMERecord(String name,long ttl,String cname,boolean disabled) {
		
		type = 5;
		rrclass = 1;
		this.name = name;
		this.ttl = ttl;
		this.disabled = disabled;
		
		ByteArrayOutputStream targetStream = new ByteArrayOutputStream();
		String[] targetArray = cname.split("\\.");
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
		setRdata(targetStream.toByteArray());
		rdlength = targetStream.size();
		
		
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
