package me.jaks.jdns.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MonitorThread implements Runnable{
	
	private Counter counter;
	
	public MonitorThread(Counter counter) {
		this.counter = counter;
	}
	@Override
	public void run() {
		
	
		ServerSocket listener;
		try {
			listener = new ServerSocket(5552);
			while(true) {
				Socket socket = listener.accept();
				OutputStream os = socket.getOutputStream();
				PrintWriter out = new PrintWriter(os,true);
				out.println("\njDNS Statistics\n");
				out.println("request = " + counter.getRequest());
				out.println("response = " + counter.getResponse());
				out.println("reqTCP = " + counter.getReqTCP());
				out.println("respTCP = " + counter.getRespTCP());
				out.println("noError = " + counter.getNoError());
				out.println("nxdomain = " + counter.getNxdomain());
				out.println("NotImplemented = " + counter.getNotImplemented());
				out.println("\n");
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		/*
		while(true) {
			System.out.println("request = " + counter.getRequest());
			System.out.println("response = " + counter.getResponse());
			System.out.println("reqTCP = " + counter.getReqTCP());
			System.out.println("respTCP = " + counter.getRespTCP());
			System.out.println("noError = " + counter.getNoError());
			System.out.println("nxdomain = " + counter.getNxdomain());
			System.out.println("NotImplemented = " + counter.getNotImplemented());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} */
		
		
	}

}
