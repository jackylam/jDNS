package me.jaks.jdns.server;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DNServer {

	/**
	 * @param args
	 * @throws PropertyVetoException 
	 */
	
	
	
	public static void main(String[] args) throws PropertyVetoException {
		
		ComboPooledDataSource ds = new ComboPooledDataSource();
		Properties prop = new Properties(); 
		
		 try {
		        //load a properties file from class path, inside static method
			 	FileInputStream in = new FileInputStream("config.properties");
		        prop.load(in);
		        in.close();
		        
		        int maxThreads = Integer.parseInt(prop.getProperty("maxthreads"));
		        int port = Integer.parseInt(prop.getProperty("port"));
		        String nameserver = prop.getProperty("nameserver");
		        String url = prop.getProperty("url");
		        String dbuser = prop.getProperty("dbuser");
		        String dbpass = prop.getProperty("dbpass");
		        
		        //configure datasource
		        ds.setDriverClass( "com.mysql.jdbc.Driver" );           
				ds.setJdbcUrl(url);
				ds.setUser(dbuser);                                  
				ds.setPassword(dbpass);                                  
				ds.setMinPoolSize(5);                                     
				ds.setAcquireIncrement(5);
				ds.setMaxPoolSize(200);
				
			
				Counter counter = new Counter();
	            
				//start connectors
				(new Thread(new UdpConnector(maxThreads,port,ds,nameserver,counter))).start();
				(new Thread(new TcpConnector(maxThreads,port,ds,nameserver,counter))).start();
				(new Thread(new MonitorThread(counter))).start();
				(new Thread(new JsonThread(counter))).start();
		        } 
		   catch (IOException ex) {
		        ex.printStackTrace();
		    }
		
		
		
	}
}
