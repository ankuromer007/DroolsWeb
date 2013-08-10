package com.neevtech.droolsweb.rs.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.neevtech.droolsweb.dao.ItemManager;
import com.neevtech.droolsweb.dao.UserManager;
import com.neevtech.droolsweb.dao.impl.ItemManagerImpl;
import com.neevtech.droolsweb.dao.impl.UserManagerImpl;
import com.neevtech.droolsweb.model.ItemBean;
import com.neevtech.droolsweb.model.UserBean;
import com.neevtech.droolsweb.services.impl.RulesEngineImpl;

@Path("/discount")
public class UserTransactionService {
	private static final Logger logger = Logger.getLogger(UserTransactionService.class);
	
	@GET
	@Path("/{user}/{item}")
	@Produces(MediaType.APPLICATION_XML)
	public ItemBean showDiscountedPrice(@PathParam("user") String userId, @PathParam("item") int itemId){
		UserManager userManager = new UserManagerImpl();
		UserBean user = userManager.getUserDetails(userId);
		
		if(user.getName() == null) {
			itemId = 0;
			logger.info("User details not found.");
		} else
			logger.info("User "+ user.getName() +" details found.");
			
		ItemManager itemManager = new ItemManagerImpl();
		ItemBean item = itemManager.getItem(itemId);
		
		if(item.getName() == null)
			logger.info("Item details not found.");
		else {
			logger.info("Item "+ item.getId() +" details found.");
		
			RulesEngineImpl.ksession.insert(user);
			RulesEngineImpl.ksession.insert(item);
			RulesEngineImpl.ksession.fireAllRules();
			logger.info("Discount calculated for item with id "+ item.getId() +" for user "+ user.getName() +".");
		}
		return item;
	}
}
