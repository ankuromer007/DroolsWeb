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
import com.neevtech.droolsweb.util.ComputeMD5Hash;
import com.neevtech.droolsweb.util.Constants;

public class LoginServlet extends HttpServlet{
	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
	
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.addHeader("Pragma", "no-cache");
		res.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		res.addHeader("Cache-Control", "pre-check=0,post-check=0");
		res.setDateHeader("Expires", 0);
		
		HttpSession session = req.getSession(true);
		if (session == null) 
			res.sendRedirect("/DroolsWeb/index");
		if (!session.getId().equals(req.getParameter("sessId"))) {
			session.invalidate();
			res.sendRedirect("/DroolsWeb/index");
		} else {
			try {
				String userId = (String) req.getParameter("uname");
				String password = (String) req.getParameter("pass");
				String passwordMD5=ComputeMD5Hash.md5(password);
				
				UserManager umi = new UserManagerImpl();
				LoginBean login = umi.isUserExists(userId);
				if(login.getUserId() != null) {
					if(login.getPassword().equals(passwordMD5)) {
						UserBean user = umi.getUserDetails(userId);
						logger.info("User {} is authenticated.", user.getName());
						
						ItemManager itemManager = new ItemManagerImpl();
						List<ItemBean> items = itemManager.getAllItems();
						
						RulesEngine rulesEngine = (RulesEngine)getServletContext().getAttribute(Constants.RulesEngine);
						rulesEngine.executeRules(user, items);
						
						session.setAttribute(Constants.User, user);
						session.setAttribute(Constants.Items, items);
						
						RequestDispatcher dispatch = this.getServletContext()
								.getRequestDispatcher("/home");
						dispatch.forward(req, res);
					} else {
						logger.info("User is not authenticated.");
						req.setAttribute("loginError","Username or password you provided does not match.");
						req.setAttribute("usernameValue",userId);
						RequestDispatcher dispatch = this.getServletContext()
								.getRequestDispatcher("/index");
						dispatch.forward(req, res);
					}
				} else {
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
