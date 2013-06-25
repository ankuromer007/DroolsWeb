package com.neevtech.droolsweb.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neevtech.droolsweb.dao.ItemManager;
import com.neevtech.droolsweb.dao.UserManager;
import com.neevtech.droolsweb.dao.impl.ItemManagerImpl;
import com.neevtech.droolsweb.dao.impl.UserManagerImpl;
import com.neevtech.droolsweb.model.ItemBean;
import com.neevtech.droolsweb.model.LoginBean;
import com.neevtech.droolsweb.model.UserBean;
import com.neevtech.droolsweb.services.RulesEngine;
import com.neevtech.droolsweb.services.impl.RulesEngineImpl;
import com.neevtech.droolsweb.util.ComputeMD5Hash;
import com.neevtech.droolsweb.util.Constants;

public class LoginServlet extends HttpServlet{
	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
	
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
//Adding header to Clear Cache before creating a new session.
		res.addHeader("Pragma", "no-cache");
		res.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		res.addHeader("Cache-Control", "pre-check=0,post-check=0");
		res.setDateHeader("Expires", 0);
//Creating Session		
		HttpSession session = req.getSession(true);
//Checking if session is created.
		if (session == null) 
			res.sendRedirect("/DroolsWeb/index");
		
		if (!session.getId().equals(req.getParameter("sessId"))) {
			session.invalidate();
			res.sendRedirect("/DroolsWeb/index");
		} else {
			try {
//Fetching Username, Password and Remember Me from JSP page(login.jsp)				
				String userId = (String) req.getParameter("uname");
				String password = (String) req.getParameter("pass");
//********Computing Hash Value of Username and password using MD5 method*********
				String passwordMD5=ComputeMD5Hash.md5(password);
//Calling UserManager for finding that user exist or not
//and forwarding the request to appropriate page.
				UserManager umi = new UserManagerImpl();
				LoginBean login = umi.isUserExists(userId);
//Checking User ID is correct or not
				if(login.getUserId() != null) {
//If User ID is correct then checking for password
					if(login.getPassword().equals(passwordMD5)) {
						UserBean user = umi.getUserDetails(userId);
						logger.info("User "+user.getName()+" is authenticated.");
//Setting RulesEngine for implementing rules
						RulesEngine rulesEngine = new RulesEngineImpl();
						StatefulKnowledgeSession ksession = rulesEngine.setupRulesEngine();
//Getting Item list for setting items in knowledge base
						ItemManager itemManager = new ItemManagerImpl();
						List<ItemBean> items = itemManager.getAllItems();
//Executing rules written in Rule File
						rulesEngine.executeRules(user, items, ksession);
						
						session.setAttribute(Constants.User, user);
						session.setAttribute(Constants.Items, items);
						
						RequestDispatcher dispatch = this.getServletContext()
								.getRequestDispatcher("/home");
						dispatch.forward(req, res);
					} else {
//If User ID is correct but password is wrong
						logger.info("User is not authenticated.");
						req.setAttribute("loginError","Username or password you provided does not match.");
						req.setAttribute("usernameValue",userId);
						RequestDispatcher dispatch = this.getServletContext()
								.getRequestDispatcher("/index");
						dispatch.forward(req, res);
					}
				} else {
//If User ID is wrong
					logger.info("User ID entered is not correct.");
					req.setAttribute("loginError","Username or password you provided does not match.");
					RequestDispatcher dispatch = this.getServletContext()
							.getRequestDispatcher("/index");
					dispatch.forward(req, res);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
