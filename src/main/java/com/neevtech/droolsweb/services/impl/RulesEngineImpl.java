package com.neevtech.droolsweb.services.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.event.knowledgebase.AfterRuleAddedEvent;
import org.drools.event.knowledgebase.AfterRuleRemovedEvent;
import org.drools.event.knowledgebase.DefaultKnowledgeBaseEventListener;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.neevtech.droolsweb.model.ItemBean;
import com.neevtech.droolsweb.model.UserBean;
import com.neevtech.droolsweb.services.RulesEngine;

public class RulesEngineImpl implements RulesEngine {
	private static final Logger logger = Logger.getLogger(RulesEngineImpl.class);
	public static StatefulKnowledgeSession ksession = null;

	public void setupRulesEngine(){
		try {
			KnowledgeBase kbase = readKnowledgeBase();
			kbase.addEventListener(new DefaultKnowledgeBaseEventListener(){
				@Override
				public void afterRuleRemoved(AfterRuleRemovedEvent event) {
					logger.info("*-*-*-*-*-afterRuleRemoved*-*-*-*-*-");
					logger.info("RuleName(in afterRemoved): "+event.getRule().getName());
					logger.info("PackageName(in afterRemoved): "+event.getRule().getPackageName());
					logger.info("*-*-*-*-*-afterRuleRemoved*-*-*-*-*-\n\n\n");
				}
				@Override
				public void afterRuleAdded(AfterRuleAddedEvent event) {
					logger.info("*-*-*-*-*-afterRuleAdded*-*-*-*-*-");
					logger.info("RuleName(in afterAdded): "+event.getRule().getName());
					logger.info("PackageName(in afterAdded): "+event.getRule().getPackageName());
					logger.info("*-*-*-*-*-afterRuleAdded*-*-*-*-*-\n\n\n");
				}
			});
			ksession = kbase.newStatefulKnowledgeSession();
			ksession.setGlobal("logger", logger);
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	private KnowledgeBase readKnowledgeBase() throws Exception {
		ResourceChangeScannerConfiguration scannerConfig = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
		scannerConfig.setProperty("drools.resource.scanner.interval", "10");
		ResourceFactory.getResourceChangeScannerService().configure(scannerConfig);

		KnowledgeAgentConfiguration kaConfig = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
		kaConfig.setProperty("drools.agent.newInstance", "false");
		KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("kagent", kaConfig);
		//kagent.setSystemEventListener(new PrintStreamSystemEventListener());
		
		//kagent.applyChangeSet(ResourceFactory.newClassPathResource("changeset.xml"));
		kagent.applyChangeSet(ResourceFactory.newClassPathResource("ChangeSet.xml"));

		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();
		return kagent.getKnowledgeBase();
    }
	
	public void executeRules(UserBean user, List<ItemBean> items) {
		for (ItemBean item : items) {
			ksession.insert(user);
			ksession.insert(item);
			ksession.fireAllRules();
		}
	}
}
