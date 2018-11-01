package com.github.bioinfo.webes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.bioinfo.webes.dao.account.AccountDao;
import com.github.bioinfo.webes.entity.Account;

@Service
public class AccountServiceImp implements AccountService {

	@Autowired
	private AccountDao accountDao;
	
	@Override
	public void save(Account user) {

		accountDao.save(user);
		
	}

	@Override
	public Account findByUserName(String userName) {
		return accountDao.findAccount(userName);
	}

}
