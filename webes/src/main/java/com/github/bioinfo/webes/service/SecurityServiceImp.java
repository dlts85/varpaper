package com.github.bioinfo.webes.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImp implements SecurityService {

	public String findLoggedUsernameAndAuth() {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Collection<?extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		String authority = "";
		if(!authorities.isEmpty()) {
			authority = authorities.iterator().next().getAuthority();
			
		}
		if(!username.equals("anonymousUser")) {
			return username + "/" + authority;

		}
	 	return null;
	}

}
