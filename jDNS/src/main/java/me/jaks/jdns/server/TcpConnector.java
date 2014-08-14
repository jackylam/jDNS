package me.jaks.jdns.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TcpConnector implements Runnable{

	private static final Logger logger = Logger.getLogger(TcpConnector.class);
	private int maxThreads;
	private int listeningPort;
	private ComboPooledDataSource ds;
	private String nameserver;
	private Counter counter;
	
	public TcpConnector(int maxThreads,int port,ComboPooledDataSource ds,String nameserver,Counter counter) {
		this.maxThreads = maxThreads;
		this.listeningPort = port;
		this.ds = ds;
		this.nameserver = nameserver;
		this.counter = counter;
	}
	@Override
	public void run() {
		
		logger.info("Starting TCP listener on port " + listeningPort);
		
		try {
			ExecutorService pool = Executors.newFixedThreadPool(maxThreads);                         
			ServerSocket listener = new ServerSocket(listeningPort);
			while(true) {
				Socket socket = listener.accept();
				counter.setReqTCP();
				TcpWorker worker = new TcpWorker(socket,ds,nameserver,counter);
				pool.execute(worker);
			}
			
		} catch (SocketException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}
		
	}

}
