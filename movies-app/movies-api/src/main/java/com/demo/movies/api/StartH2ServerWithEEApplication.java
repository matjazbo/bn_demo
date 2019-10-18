package com.demo.movies.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

import com.kumuluz.ee.EeApplication;

public class StartH2ServerWithEEApplication extends EeApplication {

	private static final Logger logger = LogManager.getLogger(StartH2ServerWithEEApplication.class);
	
    public static void main(String args[]) {
		logger.info("Starting H2 memory server");
		try {
			Server server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8091", "-ifNotExists");
			server.start();
		} catch (Exception e) {
			logger.error("Error starting H2 database server.", e);
		}
		
		new EeApplication();
    }
	
}
