package com.neevtech.droolsweb.dao;

import java.util.List;

import com.neevtech.droolsweb.model.ItemBean;

public interface ItemManager {
	public List<ItemBean> getAllItems();
	public ItemBean getItem(int id);
}
