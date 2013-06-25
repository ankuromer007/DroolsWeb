package com.neevtech.droolsweb.services;

import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;

import com.neevtech.droolsweb.model.ItemBean;
import com.neevtech.droolsweb.model.UserBean;

public interface RulesEngine {
	public StatefulKnowledgeSession setupRulesEngine();
	public void executeRules(UserBean user, List<ItemBean> items, StatefulKnowledgeSession ksession);
}
