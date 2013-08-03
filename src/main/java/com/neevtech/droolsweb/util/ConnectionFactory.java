package com.neevtech.droolsweb.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionFactory {
	
	public Connection getConnection() throws NamingException, SQLException {
		Context ctx = new InitialContext();
		//javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("java:comp/env/jdbc/droolsWeb");
		javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("java:jboss/datasources/DroolsWeb");
		return ds.getConnection();
	}
}
