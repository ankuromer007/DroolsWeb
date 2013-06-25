package com.neevtech.droolsweb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.neevtech.droolsweb.dao.UserManager;
import com.neevtech.droolsweb.model.LoginBean;
import com.neevtech.droolsweb.model.UserBean;
import com.neevtech.droolsweb.util.ConnectionFactory;

public class UserManagerImpl implements UserManager {
	PreparedStatement ptmt = null;
	Connection con = null;
	ResultSet rs=null;
	int status=0;
	
	public LoginBean isUserExists(String userId) {
		LoginBean login = new LoginBean();
		try {
			ConnectionFactory cf = new ConnectionFactory();
			con = cf.getConnection();

			String sql = "SELECT * FROM users where user_name=?";
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, userId);
			rs = ptmt.executeQuery();
			
			if (rs.next()) {
				login.setUserId(rs.getString("user_name"));
				login.setPassword(rs.getString("password"));
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (NamingException ne) {
			ne.printStackTrace();
		} finally {
			try {
				if (ptmt != null)
					ptmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return login;
	}
	
	public UserBean getUserDetails(String userId) {
		UserBean user = new UserBean();
		user.setUserId(userId);
		try {
			ConnectionFactory cf = new ConnectionFactory();
			con = cf.getConnection();
			
			String sql = "update users set updated_at=UTC_TIMESTAMP() where user_name=?";
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, userId);
			status = ptmt.executeUpdate();

			sql = "SELECT * FROM users where user_name=?";
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, userId);
			rs = ptmt.executeQuery();

			if (rs.next()) {
				user.setName(rs.getString("name"));
				user.setLocation(rs.getString("location"));
				user.setLastLogin(rs.getTimestamp("updated_at"));
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (NamingException ne) {
			ne.printStackTrace();
		} finally {
			try {
				if (ptmt != null)
					ptmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}
}
