package me.jaks.jdns.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class Header {
	
	private int id;
	private byte qr;
	private byte opcode;
	private byte aa;
	private byte tc;
	private byte rd;
	private byte ra;
	private byte z;
	private byte ad;
	private byte cd;
	private byte rcode;
	private int qdcount;
	private int ancount;
	private int nscount;
	private int arcount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte getQr() {
		return qr;
	}
	public void setQr(byte qr) {
		this.qr = qr;
	}
	public byte getOpcode() {
		return opcode;
	}
	public void setOpcode(byte opcode) {
		this.opcode = opcode;
	}
	public byte getAa() {
		return aa;
	}
	public void setAa(byte aa) {
		this.aa = aa;
	}
	public byte getTc() {
		return tc;
	}
	public void setTc(byte tc) {
		this.tc = tc;
	}
	public byte getRd() {
		return rd;
	}
	public void setRd(byte rd) {
		this.rd = rd;
	}
	public byte getRa() {
		return ra;
	}
	public void setRa(byte ra) {
		this.ra = ra;
	}
	public byte getZ() {
		return z;
	}
	public void setZ(byte z) {
		this.z = z;
	}
	public byte getAd() {
		return ad;
	}
	public void setAd(byte ad) {
		this.ad = ad;
	}
	public byte getCd() {
		return cd;
	}
	public void setCd(byte cd) {
		this.cd = cd;
	}
	public byte getRcode() {
		return rcode;
	}
	public void setRcode(byte rcode) {
		this.rcode = rcode;
	}
	public int getQdcount() {
		return qdcount;
	}
	public void setQdcount(int qdcount) {
		this.qdcount = qdcount;
	}
	public int getAncount() {
		return ancount;
	}
	public void setAncount(int ancount) {
		this.ancount = ancount;
	}
	public int getNscount() {
		return nscount;
	}
	public void setNscount(int nscount) {
		this.nscount = nscount;
	}
	public int getArcount() {
		return arcount;
	}
	public void setArcount(int arcount) {
		this.arcount = arcount;
	}
	
	public byte[] serialize() {
		
		byte[] header = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] idBytes = new byte[] {(byte)(id >>> 8),(byte)(id)};
		byte[] qdBytes = new byte[] {(byte)(qdcount >>> 8),(byte)(qdcount)};
		byte[] anBytes = new byte[] {(byte)(ancount >>> 8),(byte)(ancount)};
		byte[] nsBytes = new byte[] {(byte)(nscount >>> 8),(byte)(nscount)};
		byte[] arBytes = new byte[] {(byte)(arcount >>> 8),(byte)(arcount)};
		
		byte[] flagsBytes = new byte[2];
		
		flagsBytes[0] = (byte)((qr << 7 & 0x80) | (opcode << 3 & 0x78) | (aa << 2 & 0x04) | (tc << 1 & 0x02) | (rd & 0x1));
		flagsBytes[1] = (byte)((ra << 7 & 0x80) | (z << 6 & 0x40) | (ad << 5 & 0x20) | (cd << 4 & 0x10) | (rcode & 0x0f));
		
		try {
			baos.write(idBytes);
			baos.write(flagsBytes);
			baos.write(qdBytes);
			baos.write(anBytes);
			baos.write(nsBytes);
			baos.write(arBytes);
			
			header = baos.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return header;
		
	}
	
}
