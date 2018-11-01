package com.github.bioinfo.webes.dao.account;

import com.github.bioinfo.webes.entity.Account;

public interface AccountDao {

	public Account findAccount(String name);
	
	public void save(Account account);
}
