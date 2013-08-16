package com.neevtech.droolsweb.services.impl;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
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
			ksession = kbase.newStatefulKnowledgeSession();
			ksession.setGlobal("logger", logger);
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	/*private KnowledgeBase readKnowledgeBase() throws Exception {
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
    }*/
	
	private KnowledgeBase readKnowledgeBase() throws Exception {
		ResourceChangeScannerConfiguration scannerConfig = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
		scannerConfig.setProperty("drools.resource.scanner.interval", "10");
		ResourceFactory.getResourceChangeScannerService().configure(scannerConfig);

		KnowledgeAgentConfiguration kaConfig = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
		kaConfig.setProperty("drools.agent.newInstance", "false");
		KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("myagent", kaConfig);
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("admin", "admin".toCharArray());
			}
		});
		kagent.applyChangeSet(ResourceFactory.newUrlResource("http://localhost:8080/guvnor/org.drools.guvnor.Guvnor/package/DroolsWeb/LATEST/ChangeSet.xml"));
		//kagent.applyChangeSet(ResourceFactory.newClassPathResource("changeset.xml"));

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
