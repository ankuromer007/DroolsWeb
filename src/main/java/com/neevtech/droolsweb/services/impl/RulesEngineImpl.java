package com.neevtech.droolsweb.services.impl;

import java.io.File;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neevtech.droolsweb.model.ItemBean;
import com.neevtech.droolsweb.model.UserBean;
import com.neevtech.droolsweb.services.RulesEngine;

public class RulesEngineImpl implements RulesEngine {
	private static final Logger logger = LoggerFactory.getLogger(RulesEngineImpl.class);
	//KnowledgeRuntimeLogger logger = null;

	public StatefulKnowledgeSession setupRulesEngine(){
		StatefulKnowledgeSession ksession = null;
		try {
			KnowledgeBase kbase = readKnowledgeBase();
			ksession = kbase.newStatefulKnowledgeSession();
			//logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        } catch (Throwable t) {
            t.printStackTrace();
        }
		return ksession;
	}
	
	private KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        File drlFile = new File(System.getProperty("user.home")+"/workspace/DroolsWeb/src/main/resources/discountRule.drl");
        kbuilder.add(ResourceFactory.newFileResource(drlFile), ResourceType.DRL);
        //kbuilder.add(ResourceFactory.newClassPathResource("discountRule.drl"), ResourceType.DRL); 
        
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        return kbase;
    }
	
	public void executeRules(UserBean user, List<ItemBean> items, StatefulKnowledgeSession ksession) {
		ksession.setGlobal("logger", logger);
		ksession.insert(user);
		for (ItemBean item : items) {
			ksession.insert(item);
		}
		ksession.fireAllRules();
		//logger.close();
	}
}
