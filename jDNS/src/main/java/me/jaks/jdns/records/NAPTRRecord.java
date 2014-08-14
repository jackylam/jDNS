package me.jaks.jdns.records;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class NAPTRRecord implements NAPTR {

	private String name;
	private int type;
	private int rrclass;
	private long ttl;
	private int rdlength;
	private byte[] rdata;
	private int order;
	private int pref;
	private String flags;
	private String services;
	private String regex;
	private String replace;
	private boolean disabled;
	
	public NAPTRRecord() {
		
	}
	
	public NAPTRRecord(String name,long ttl,int rrclass,int order,int pref,String flags,String services,String regex,String replace,boolean disabled) {
		
		this.name = name;
		this.type = 35;
		this.rrclass = rrclass;
		this.ttl = ttl;
		this.order = order;
		this.pref = pref;
		this.flags = flags;
		this.services = services;
		this.regex = regex;
		this.replace = replace;
		this.disabled = disabled;
		
		byte[] orderBytes = new byte[2];
		byte[] prefBytes = new byte[2];
		
		orderBytes[0] = (byte)(order >>> 8);
		orderBytes[1] = (byte) order;
		prefBytes[0] = (byte)(pref >>> 8);
		prefBytes[1] = (byte) pref;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			byte[] flagsBytes = flags.getBytes("US-ASCII");
			byte[] servicesBytes = services.getBytes("US-ASCII");
			byte[] regexBytes = regex.getBytes("US-ASCII");
			
			ByteArrayOutputStream replaceStream = new ByteArrayOutputStream();
			String[] replaceArray = replace.split("\\.");
			for(String label:replaceArray) {
				int temp = label.length();
				replaceStream.write((byte) temp);
				try {
					replaceStream.write(label.getBytes("US-ASCII"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			replaceStream.write((byte)0);
			byte[] replaceBytes = replaceStream.toByteArray();
			
			baos.write(orderBytes);
			baos.write(prefBytes);
			baos.write((byte)flagsBytes.length);
			baos.write(flagsBytes);
			baos.write((byte)servicesBytes.length);
			baos.write(servicesBytes);
			baos.write((byte)regexBytes.length);
			baos.write(regexBytes);
			baos.write(replaceBytes);
			
			setRdata(baos.toByteArray());
			rdlength = baos.size();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public int getOrder() {
		return order;
	}

	@Override
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getPreference() {
		return pref;
	}

	@Override
	public void setPreference(int pref) {
		this.pref = pref;
	}

	@Override
	public String getFlags() {
		return flags;
	}

	@Override
	public void setFlags(String flags) {
		this.flags = flags;
	}

	@Override
	public String getServices() {
		return services;
	}

	@Override
	public void setServices(String services) {
		this.services = services;
	}

	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public String getReplacement() {
		return replace;
	}

	@Override
	public void setReplacement(String replace) {
		this.replace = replace;
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
