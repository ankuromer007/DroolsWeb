package com.neevtech.droolsweb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.neevtech.droolsweb.dao.ItemManager;
import com.neevtech.droolsweb.model.ItemBean;
import com.neevtech.droolsweb.util.ConnectionFactory;

public class ItemManagerImpl implements ItemManager {
	PreparedStatement ptmt = null;
	Connection con = null;
	ResultSet rs=null;
		
	public ArrayList<ItemBean> getAllItems() {
		ArrayList<ItemBean> items = new ArrayList<ItemBean>(); 
		try {
			ConnectionFactory cf = new ConnectionFactory();
			con = cf.getConnection();
			
			String sql = "SELECT * FROM items";
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			
			while(rs.next()) {
				ItemBean item= new ItemBean();
				
				item.setId(rs.getInt("id"));
				item.setName(rs.getString("name"));
				item.setBasePrice(rs.getFloat("price"));
				
				items.add(item);
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
		return items;
	}
	
	public ItemBean getItem(int id) {
		ItemBean item= new ItemBean();
		try {
			ConnectionFactory cf = new ConnectionFactory();
			con = cf.getConnection();
			
			String sql = "SELECT * FROM items where id=?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			rs = ptmt.executeQuery();
			
			if (rs.next()) {
				item.setId(rs.getInt("id"));
				item.setName(rs.getString("name"));
				item.setBasePrice(rs.getFloat("price"));
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
		return item;
	}
}
