package com.github.bioinfo.webes.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import com.github.bioinfo.webes.dao.account.AccountDao;
import com.github.bioinfo.webes.entity.Account;

/**
 * 用户登录校验
 * @author fujian
 *
 */
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private AccountDao accountDao;
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account account = accountDao.findAccount(username);
		System.out.println("Account=" + account);
		
		if(null == account) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		
		String role = account.getUserRole();
		
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority(role);
		grantList.add(authority);
		
		boolean enabled = account.isActive();
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
		UserDetails userDetails = new User(account.getUserName(), account.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantList);
		
		return userDetails;
	}

}
