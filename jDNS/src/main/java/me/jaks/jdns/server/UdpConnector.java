package me.jaks.jdns.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class UdpConnector implements Runnable {

	private static final Logger logger = Logger.getLogger(UdpConnector.class);
	private int maxThreads;
	private int listeningPort;
	private ComboPooledDataSource ds;
	private String nameserver;
	private Counter counter;
	
	public UdpConnector(int maxThreads,int port,ComboPooledDataSource ds,String nameserver,Counter counter) {
		this.maxThreads = maxThreads;
		this.listeningPort = port;
		this.ds = ds;
		this.nameserver = nameserver;
		this.counter = counter;
	}
	@Override
	public void run() {
		DatagramSocket socket;
		
		logger.info("Starting UDP listener on port " + listeningPort);
		
		try {
			ExecutorService pool = Executors.newFixedThreadPool(maxThreads);           
			socket = new DatagramSocket(listeningPort);
			while(true) {
				byte[] buf = new byte[512];
				DatagramPacket packet = new DatagramPacket(buf,512);
				socket.receive(packet);
				counter.setRequest();
				UdpWorker worker = new UdpWorker(socket,packet,ds,nameserver,counter);
				pool.execute(worker);
			}
		} catch (SocketException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}
		
	}

}
