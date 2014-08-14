package me.jaks.jdns.server;

public class Counter{
	
	/**
	 * 
	 */
	private int request = 0;
	private int response = 0;
	private int reqTCP = 0;
	private int respTCP = 0;
	private int noError = 0;
	private int nxdomain = 0;
	private int notImplemented = 0;
	
	public Counter() {
		// TODO Auto-generated constructor stub
	}
	
	public int getRequest() {
		return request;
	}
	public synchronized void setRequest() {
		this.request++;
	}
	public int getResponse() {
		return response;
	}
	public synchronized void setResponse() {
		this.response++;
	}
	public int getReqTCP() {
		return reqTCP;
	}
	public synchronized void setReqTCP() {
		this.reqTCP++;
	}
	public int getRespTCP() {
		return respTCP;
	}
	public synchronized void setRespTCP() {
		this.respTCP++;
	}
	public int getNoError() {
		return noError;
	}
	public synchronized void setNoError() {
		this.noError++;
	}
	public int getNxdomain() {
		return nxdomain;
	}
	public synchronized void setNxdomain() {
		this.nxdomain++;
	}
	public int getNotImplemented() {
		return notImplemented;
	}
	public synchronized void setNotImplemented() {
		this.notImplemented++;
	}
	
	
	
}
