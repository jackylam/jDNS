package me.jaks.jdns.webapp.rest;

import me.jaks.jdns.webapp.RecordDao;
import me.jaks.jdns.webapp.RecordDaoInt;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;


public class RestApplication extends GuiceServletContextListener {

	 @Override
	 protected Injector getInjector() {
	 return Guice.createInjector( new ServletModule() {
	 @Override
	 protected void configureServlets() {
	 
		 bind(RecordDaoInt.class).to(RecordDao.class);
		 ResourceConfig rc = new PackagesResourceConfig( "me.jaks.jdns.webapp" );
		 for ( Class<?> resource : rc.getClasses() ) {
			 bind( resource );
		 }
	 serve( "/rest/*" ).with( GuiceContainer.class );
	 }
	 } );
	 }
	}