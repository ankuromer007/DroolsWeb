package com.neevtech.droolsweb.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.neevtech.droolsweb.services.RulesEngine;
import com.neevtech.droolsweb.services.impl.RulesEngineImpl;

public class AppServletContextListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(AppServletContextListener.class);
	ServletContext context;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("ServletContextListener started");
		context = sce.getServletContext();
		RulesEngine rulesEngine = new RulesEngineImpl();
		rulesEngine.setupRulesEngine();
		context.setAttribute(Constants.RulesEngine, rulesEngine);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		this.context = null;
		logger.info("ServletContextListener destroyed");
	}
}
