package com.neevtech.droolsweb.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.neevtech.droolsweb.model.UserBean;
import com.neevtech.droolsweb.util.Constants;
import com.neevtech.droolsweb.util.SessionServlet;

public class LogoutServlet extends SessionServlet {
	private static final Logger logger = Logger.getLogger(LogoutServlet.class);
	
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {	
		try {
			HttpSession session = req.getSession();
			logger.info("User "+ ((UserBean)session.getAttribute(Constants.User)).getName() +" logout successfully.");
			session.invalidate();
			res.sendRedirect("/DroolsWeb/index");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
