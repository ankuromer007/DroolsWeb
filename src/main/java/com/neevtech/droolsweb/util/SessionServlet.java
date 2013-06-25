package com.neevtech.droolsweb.util;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class SessionServlet extends HttpServlet {
	
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {	
		this.doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		clearCache(res);
		execute(req,res);
	}
	
	private void clearCache(HttpServletResponse servletResponse) {
		servletResponse.addHeader("Pragma","no-cache");
		servletResponse.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
		servletResponse.addHeader("Cache-Control","pre-check=0,post-check=0");
		servletResponse.setDateHeader("Expires",0);
	}
	
	abstract public void execute(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException;
}
