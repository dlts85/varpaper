package com.github.bioinfo.webes.service;

import com.github.bioinfo.webes.entity.Account;

public interface AccountService {

	public void save(Account user);
	
	public Account findByUserName(String userName);
}
