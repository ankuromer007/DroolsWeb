package com.neevtech.droolsweb.services;

import java.util.List;

import com.neevtech.droolsweb.model.ItemBean;
import com.neevtech.droolsweb.model.UserBean;

public interface RulesEngine {
	public void setupRulesEngine();
	public void executeRules(UserBean user, List<ItemBean> items);
}
