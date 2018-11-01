package com.github.bioinfo.webes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		
		//����/query��Ҫ�û���¼
		http.authorizeRequests().antMatchers("/query")
		.authenticated();
		
		//�������������Ҫ�û���¼
		http.authorizeRequests().antMatchers("/api/v1/pmid2title/publications")
		.authenticated();
		
		
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		
		http
		.authorizeRequests()
		.and()
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/request_for_login")
		.defaultSuccessUrl("/query")
		.failureUrl("/login?error")
		.usernameParameter("userName")
		.passwordParameter("password")
		.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout");
		
		//ͬһ�û�����ͬʱ�ദ��½
		http.sessionManagement().maximumSessions(1).expiredUrl("/login");
	}

}
