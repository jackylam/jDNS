package me.jaks.jdns.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

public class JsonThread implements Runnable{

private Counter counter;
	
	public JsonThread(Counter counter) {
		this.counter = counter;
	}
	@Override
	public void run() {
		
	
		ServerSocket listener;
		try {
			listener = new ServerSocket(5551);
			while(true) {
				Socket socket = listener.accept();
				OutputStream os = socket.getOutputStream();
				PrintWriter out = new PrintWriter(os,true);
				
				Gson gson = new Gson();
				String json = gson.toJson(counter);
				
				out.println(json);
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
