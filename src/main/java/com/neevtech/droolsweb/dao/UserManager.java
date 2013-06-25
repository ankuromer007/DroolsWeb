package com.neevtech.droolsweb.dao;

import com.neevtech.droolsweb.model.LoginBean;
import com.neevtech.droolsweb.model.UserBean;

public interface UserManager {
	public LoginBean isUserExists(String userId);
	public UserBean getUserDetails(String userId);
}
