package com.neevtech.droolsweb.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neevtech.droolsweb.services.RulesEngine;
import com.neevtech.droolsweb.services.impl.RulesEngineImpl;

public class AppServletContextListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(AppServletContextListener.class);
	ServletContext context;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("AppServletContextListener started");
		context = sce.getServletContext();
		RulesEngine rulesEngine = new RulesEngineImpl();
		StatefulKnowledgeSession ksession = rulesEngine.setupRulesEngine();
		context.setAttribute(Constants.RulesEngine, rulesEngine);
		context.setAttribute(Constants.Ksession, ksession);
		logger.info("RulesEngineObject: {}", rulesEngine.hashCode());
		logger.info("KsessionObject: {}", ksession.hashCode());
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		((StatefulKnowledgeSession) context.getAttribute(Constants.Ksession)).dispose();
		this.context = null;
		logger.info("AppServletContextListener destroyed");
	}
}
