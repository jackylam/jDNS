package me.jaks.jdns.server;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.jaks.jdns.records.ARecord;
import me.jaks.jdns.records.NAPTRRecord;
import me.jaks.jdns.records.NSRecord;
import me.jaks.jdns.records.SRVRecord;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TcpWorker implements Runnable{

	private static final Logger logger = Logger.getLogger(TcpWorker.class);
	private Socket socket;
	private ComboPooledDataSource ds;
	private String nameserver;
	private Counter counter;
	
	public TcpWorker(Socket socket,ComboPooledDataSource ds,String nameserver,Counter counter) {
		this.socket = socket;
		this.ds = ds;
		this.nameserver = nameserver;
		this.counter = counter;
	}
	@Override
	public void run() {
		
		try {
			byte[] buf = new byte[65535];
			InputStream in = socket.getInputStream();
			int size = in.read(buf) - 2;
			byte[] data = new byte[size];
			System.arraycopy(buf, 2, data, 0, size);
			long id = Thread.currentThread().getId();
			if(logger.isDebugEnabled()) {
				logger.debug("Thread[" + id + "] Decoding query...");
				printTrace(data);
			}
			Header header = parseHeader(data);
			byte[] qtype = new byte[2];
			byte[] qclass = new byte[2];
			int pos = 12;
			int length = 0;
			while(data[pos] != 0) {
				length++;
				pos++;
			}
			length++;
			byte[] qName = new byte[length];
			System.arraycopy(data, 12, qName,0,length);
			
			pos++;
			System.arraycopy(data,pos,qtype,0,2);
			System.arraycopy(data,pos + 2, qclass,0,2);
			
			String domain = convertQnameString(qName);
			
			if(logger.isDebugEnabled())
				logger.debug("QNAME: " + domain + " qtype: " + bytesToHex(qtype) + " qclass: " + bytesToHex(qclass));

			int type = qtype[0] << 8 & 0xFF00 | qtype[1];
			
			switch(type) {
				case 1: // A Record
					ARecord[] result = getARecord(domain);
					if(result == null) {
						counter.setNxdomain();
						ByteArrayOutputStream rbufStream = new ByteArrayOutputStream();
						header.setQr((byte) 1);
						header.setRcode((byte) 3);
						try {
							rbufStream.write(header.serialize());
							byte[] rbuf = rbufStream.toByteArray();
							byte[] rbuf2 = new byte[rbuf.length + 2];
							rbuf2[0] = (byte)(rbuf.length << 8 & 0xff00);
							rbuf2[1] = (byte)(rbuf.length & 0x00ff);
							System.arraycopy(rbuf, 0, rbuf2, 2, rbuf.length);
							if(logger.isDebugEnabled()) {
								logger.debug("Deocding response...");
								printTrace(rbuf2);
							}
							OutputStream os = socket.getOutputStream();
							os.write(rbuf2);
							os.flush();
							os.close();
							counter.setRespTCP();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					else {
						// found record
						counter.setNoError();
						int resRecords = 0;
						for(ARecord record: result) {
							if (record.getDisabled() != true)
								resRecords++;
						}
						header.setQr((byte)1);
						header.setRcode((byte) 0);
						header.setAncount(resRecords);
						header.setNscount(1);
						byte[] offset = {(byte)0xC0, (byte)0x0C};
						
						ByteArrayOutputStream rbufStream = new ByteArrayOutputStream();
						
						try {
							rbufStream.write(header.serialize());
							rbufStream.write(qName);
							rbufStream.write(qtype);
							rbufStream.write(qclass);
					
							for(ARecord record: result) {
								if (record.getDisabled() == true)
									continue;
								rbufStream.write(offset);
								rbufStream.write(record.serialize());
							}
							NSRecord nsRecord = new NSRecord(domain,nameserver);
							rbufStream.write(offset);
							rbufStream.write(nsRecord.serialize());
							byte[] rbuf = rbufStream.toByteArray();
							byte[] rbuf2 = new byte[rbuf.length + 2];
							rbuf2[0] = (byte)(rbuf.length << 8 & 0xff00);
							rbuf2[1] = (byte)(rbuf.length & 0x00ff);
							System.arraycopy(rbuf, 0, rbuf2, 2, rbuf.length);
							if(logger.isDebugEnabled()) {
								logger.debug("Deocding response...");
								printTrace(rbuf2);
							}
							OutputStream os = socket.getOutputStream();
							os.write(rbuf2);
							os.flush();
							os.close();
							counter.setRespTCP();
						}catch(IOException e) {
							logger.error("",e);
						}
							
					}
					break;
						
				case 33:
					SRVRecord[] srvResult = getSRVRecord(domain);
					if(srvResult == null) {
						counter.setNxdomain();
						ByteArrayOutputStream rbufStream = new ByteArrayOutputStream();
						header.setQr((byte) 1);
						header.setRcode((byte) 3);
						try {
							rbufStream.write(header.serialize());
							byte[] rbuf = rbufStream.toByteArray();
							byte[] rbuf2 = new byte[rbuf.length + 2];
							rbuf2[0] = (byte)(rbuf.length << 8 & 0xff00);
							rbuf2[1] = (byte)(rbuf.length & 0x00ff);
							System.arraycopy(rbuf, 0, rbuf2, 2, rbuf.length);
							if(logger.isDebugEnabled()) {
								logger.debug("Deocding response...");
								printTrace(rbuf2);
							}
							OutputStream os = socket.getOutputStream();
							os.write(rbuf2);
							os.flush();
							os.close();
							counter.setRespTCP();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}		
					}
					else {
						// found record
						counter.setNoError();
						
						int resRecords = 0;
						for(SRVRecord record: srvResult) {
							if(record.getDisabled() != true)
								resRecords++;
						}
						header.setQr((byte)1);
						header.setRcode((byte) 0);
						header.setAncount(resRecords);
						header.setNscount(1);
						byte[] offset = {(byte)0xC0, (byte)0x0C};
						
						ByteArrayOutputStream rbufStream = new ByteArrayOutputStream();
						
						try {
							rbufStream.write(header.serialize());
							rbufStream.write(qName);
							rbufStream.write(qtype);
							rbufStream.write(qclass);
					
							for(SRVRecord record: srvResult) {
								if(record.getDisabled() == true)
									continue;
								rbufStream.write(offset);
								rbufStream.write(record.serialize());
							}
							NSRecord nsRecord = new NSRecord(domain,nameserver);
							rbufStream.write(offset);
							rbufStream.write(nsRecord.serialize());
							byte[] rbuf = rbufStream.toByteArray();
							byte[] rbuf2 = new byte[rbuf.length + 2];
							rbuf2[0] = (byte)(rbuf.length << 8 & 0xff00);
							rbuf2[1] = (byte)(rbuf.length & 0x00ff);
							System.arraycopy(rbuf, 0, rbuf2, 2, rbuf.length);
							if(logger.isDebugEnabled()) {
								logger.debug("Deocding response...");
								printTrace(rbuf2);
							}
							OutputStream os = socket.getOutputStream();
							os.write(rbuf2);
							os.flush();
							os.close();
							counter.setRespTCP();
						}catch(IOException e) {
							logger.error("",e);
						}
							
					}
					break;
				case 35:
					NAPTRRecord[] naptrResult = getNAPTRRecord(domain);
					if(naptrResult == null) {
						counter.setNxdomain();
						ByteArrayOutputStream rbufStream = new ByteArrayOutputStream();
						header.setQr((byte) 1);
						header.setRcode((byte) 3);
						try {
							rbufStream.write(header.serialize());
				
							byte[] rbuf = rbufStream.toByteArray();
							byte[] rbuf2 = new byte[rbuf.length + 2];
							rbuf2[0] = (byte)(rbuf.length << 8 & 0xff00);
							rbuf2[1] = (byte)(rbuf.length & 0x00ff);
							System.arraycopy(rbuf, 0, rbuf2, 2, rbuf.length);
							if(logger.isDebugEnabled()) {
								logger.debug("Deocding response...");
								printTrace(rbuf2);
							}
							OutputStream os = socket.getOutputStream();
							os.write(rbuf2);
							os.flush();
							os.close();
							counter.setRespTCP();
						} catch (IOException e) {
							logger.error("",e);
						}  
					}
					else {
						// found record
						counter.setNoError();
						int resRecords = 0;
						for(NAPTRRecord record: naptrResult) {
							if(record.getDisabled() != true)
								resRecords++;
						}
						ByteArrayOutputStream rbufStream = new ByteArrayOutputStream();
						header.setQr((byte) 1);
						header.setRcode((byte) 0);
						header.setAncount(resRecords);
						header.setNscount(1);
						byte[] offset = {(byte)0xC0, (byte)0x0C};
						
						try {
							rbufStream.write(header.serialize());
							rbufStream.write(qName);
							rbufStream.write(qtype);
							rbufStream.write(qclass);
					
							for(NAPTRRecord record: naptrResult) {
								if(record.getDisabled() == true)
									continue;
								rbufStream.write(offset);
								rbufStream.write(record.serialize());
							}
							NSRecord nsRecord = new NSRecord(domain,nameserver);
							rbufStream.write(offset);
							rbufStream.write(nsRecord.serialize());
							byte[] rbuf = rbufStream.toByteArray();
							byte[] rbuf2 = new byte[rbuf.length + 2];
							rbuf2[0] = (byte)(rbuf.length << 8 & 0xff00);
							rbuf2[1] = (byte)(rbuf.length & 0x00ff);
							System.arraycopy(rbuf, 0, rbuf2, 2, rbuf.length);
							if(logger.isDebugEnabled()) {
								logger.debug("Deocding response...");
								printTrace(rbuf2);
							}
							OutputStream os = socket.getOutputStream();
							os.write(rbuf2);
							os.flush();
							os.close();
							counter.setRespTCP();
						} catch (IOException e) {
							logger.error("",e);
						}
					}
					break;
				default:
					counter.setNotImplemented();
					ByteArrayOutputStream rbufStream = new ByteArrayOutputStream();
					header.setQr((byte) 1);
					header.setRcode((byte) 4);
					try {
						rbufStream.write(header.serialize());
					
						byte[] rbuf = rbufStream.toByteArray();
						byte[] rbuf2 = new byte[rbuf.length + 2];
						rbuf2[0] = (byte)(rbuf.length << 8 & 0xff00);
						rbuf2[1] = (byte)(rbuf.length & 0x00ff);
						System.arraycopy(rbuf, 0, rbuf2, 2, rbuf.length);
						if(logger.isDebugEnabled()) {
							logger.debug("Deocding response...");
							printTrace(rbuf2);
						}
						OutputStream os = socket.getOutputStream();
						os.write(rbuf2);
						os.flush();
						os.close();
						counter.setRespTCP();
					} catch (IOException e) {
						logger.error("",e);
					} 
			}	
		} catch(IOException e) {
			logger.error("",e);
		}
	}

private ARecord[] getARecord(String name) {
		
		boolean found = false;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<ARecord> aList = new ArrayList<ARecord>();
		
		try {
			conn = ds.getConnection();
			st = conn.prepareStatement("SELECT ttl,content,disabled from records where name = ? and type = ?");
			st.setString(1, name);
			st.setString(2, "a");
			rs = st.executeQuery();
			while(rs.next()) {
				long ttl = rs.getLong(1);
				String ipaddress = rs.getString(2);
				boolean disabled = rs.getBoolean(3);
				ARecord temp = new ARecord(name,ttl,ipaddress,disabled);
				found = true;
				aList.add(temp);
			}
			st.close();
			rs.close();
			
		} catch (SQLException e) {
			logger.error("",e);
		}
		finally {
			JDBChelper.close(rs);
			JDBChelper.close(st);
			JDBChelper.close(conn);
			
		}
			if(found == true)
				return aList.toArray(new ARecord[aList.size()]);
			else
				return null;
	}
	
	private SRVRecord[] getSRVRecord(String name) {
			
			boolean found = false;
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			ArrayList<SRVRecord> aList = new ArrayList<SRVRecord>();
			
			try {
				conn = ds.getConnection();
				st = conn.prepareStatement("SELECT ttl,priority,content,disabled from records where name = ? and type = ?");
				st.setString(1, name);
				st.setString(2,"srv");
				rs = st.executeQuery();
				while(rs.next()) {
					long ttl = rs.getLong(1);
					int rrclass = 1;
					int priority = rs.getInt(2);
					String content = rs.getString(3);
					boolean disabled = rs.getBoolean(4);
					String[] data = content.split("[ ]+");
					int weight = Integer.parseInt(data[0]);
					int port = Integer.parseInt(data[1]);
					String target = data[2];
					SRVRecord temp = new SRVRecord(name,ttl,rrclass,priority,weight,port,target,disabled);
					found = true;
					aList.add(temp);
				}
				st.close();
				rs.close();
				
			} catch (SQLException e) {
				logger.error("",e);
			}
			finally {
				JDBChelper.close(rs);
				JDBChelper.close(st);
				JDBChelper.close(conn);
				
			}
				if(found == true)
					return aList.toArray(new SRVRecord[aList.size()]);
				else
					return null;
		}
	
	private NAPTRRecord[] getNAPTRRecord(String name) {
		
		boolean found = false;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<NAPTRRecord> aList = new ArrayList<NAPTRRecord>();
		
		try {
			conn = ds.getConnection();
			
			
			st = conn.prepareStatement("SELECT ttl,content,disabled from records where name = ? and type = ?");
			st.setString(1, name);
			st.setString(2,"naptr");
			rs = st.executeQuery();
	
			while(rs.next()) {
				long ttl = rs.getLong(1);
				int rrclass = 1;
				String content = rs.getString(2);
				boolean disabled = rs.getBoolean(3);
				String[] data = content.split("[ ]+");
				int order = Integer.parseInt(data[0]);
				int pref = Integer.parseInt(data[1]);
				String flags = data[2].replaceAll("\"", "");
				String services = data[3].replaceAll("\"", "");
				String regex = data[4].replaceAll("\"","");
				String replace = data[5];
				NAPTRRecord temp = new NAPTRRecord(name,ttl,rrclass,order,pref,flags,services,regex,replace,disabled);
				found = true;
				aList.add(temp);
			}
			st.close();
			rs.close();
			
		} catch (SQLException e) {
			logger.error("",e);
		}
		finally {
			JDBChelper.close(rs);
			JDBChelper.close(st);
			JDBChelper.close(conn);
			
		}
			if(found == true)
				return aList.toArray(new NAPTRRecord[aList.size()]);
			else
				return null;
	}
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	String convertQnameString(byte[] bytes) {
		
		int pos = 0;
		StringBuffer sb = new StringBuffer();
		
		while(bytes[pos] != 0) {
			int start = pos + 1;
			byte[] buf = new byte[bytes[pos]];
			System.arraycopy(bytes, start, buf, 0, bytes[pos]);
			String label = new String(buf);
			pos = start + bytes[pos];
			sb.append(label);
			sb.append(".");
		}
		return sb.toString();
		
	}
	private void printTrace(byte[] data) {
		
		int count = 0;
		StringBuffer buf = new StringBuffer();
		while(count < data.length) {
			if(count % 16 == 0) {
				String temp = String.format("\n%04X   ", count & 0xFF);
				buf.append(temp);
			}
			String temp = String.format("%02X ", data[count]);
			buf.append(temp);
			count++;
		}
		String trace = buf.toString();
		logger.debug(trace);
	}
	
	private Header parseHeader(byte[] data) {
			
			byte[] idBytes = new byte[2];
			byte[] flagsBytes = new byte[2];
			byte[] qdcountBytes = new byte[2];
			byte[] ancountBytes = new byte[2];
			byte[] nscountBytes = new byte[2];
			byte[] arcountBytes = new byte[2];
			
			System.arraycopy(data,0,idBytes,0,2);
			System.arraycopy(data,2,flagsBytes,0,2);
			System.arraycopy(data,4,qdcountBytes,0,2);
			System.arraycopy(data,6,ancountBytes,0,2);
			System.arraycopy(data,8,nscountBytes,0,2);
			System.arraycopy(data,10,arcountBytes,0,2);
			
			int id = idBytes[0] << 8 & 0xff00 | idBytes[1] & 0x00ff;
			
			byte qr = (byte)(flagsBytes[0] >>> 7 & 0x1);
			byte opcode = (byte)(flagsBytes[0] >>> 3 & 0x0f);
			byte aa = (byte)(flagsBytes[0] >>> 10 & 0x1);
			byte tc = (byte)(flagsBytes[0] >>> 9 & 0x1);
			byte rd = (byte)(flagsBytes[0] >>> 8 & 0x1);
			byte ra = (byte)(flagsBytes[0] >>> 7 & 0x1);
			byte z = (byte)(flagsBytes[0] >>> 6 & 0x1);
			byte ad = (byte)(flagsBytes[0] >>> 5 & 0x1);
			byte cd = (byte)(flagsBytes[0] >>> 4 & 0x1);
			byte rcode = (byte)(flagsBytes[0] & 0x0f);
		
			int qdcount = qdcountBytes[0] & 0xf0 | qdcountBytes[1] & 0x0f;
			int ancount = ancountBytes[0] & 0xf0 | ancountBytes[1] & 0x0f;
			int nscount = nscountBytes[0] & 0xf0 | nscountBytes[1] & 0x0f;
			int arcount = arcountBytes[0] & 0xf0 | arcountBytes[1] & 0x0f;
			
			Header header = new Header();
			header.setId(id);
			header.setQr(qr);
			header.setOpcode(opcode);
			header.setAa(aa);
			header.setTc(tc);
			header.setRd(rd);
			header.setRa(ra);
			header.setZ(z);
			header.setAd(ad);
			header.setCd(cd);
			header.setRcode(rcode);
			header.setQdcount(qdcount);
			header.setAncount(ancount);
			header.setNscount(nscount);
			header.setArcount(arcount);
			
			if(logger.isDebugEnabled())
				logger.debug("id: " + id + " qr: " + qr + " opcode: " + opcode + " aa: " + aa + " tc: " + tc + " rd: " + rd + "ra: " + ra
						+ " z: " + z + " ad: " + ad + " cd: " + cd + " rcode: " + rcode + " QDCOUNT: " + qdcount + " ANCOUNT: " + ancount +
						" NSCOUNT: " + nscount + " ARCOUNT: " + arcount);
			return header;
			
		}
}
