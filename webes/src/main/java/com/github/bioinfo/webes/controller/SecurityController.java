package com.github.bioinfo.webes.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.bioinfo.webes.entity.Account;
import com.github.bioinfo.webes.service.AccountService;
import com.github.bioinfo.webes.service.SecurityService;
import com.github.bioinfo.webes.validation.AccountValidation;

@Controller
public class SecurityController {
	
	@Autowired
	private SecurityService securityService;
	@Autowired
	private AccountValidation accountValidation;
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value={"/login"}, method= RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if(error != null) {
			model.addAttribute("error", "Your username and password is invalid");
		}
		if(logout != null) {
			model.addAttribute("message", "You have been logged out successfully");
		}
		return "login";
	}
	
	@RequestMapping(value={"/registration"}, method= RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("accountForm", new Account());
		return "registration";
	}
	
	@RequestMapping(value={"/registration"}, method= RequestMethod.POST)
	public String registration(@ModelAttribute("accountForm") Account account, BindingResult bindingResult, Model model) {
		accountValidation.validate(account, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return "registration";
		}
		
		account.setActive(true);
		account.setUserRole("EMPOLYEE");
		accountService.save(account);
		return "redirect:login";
		
	}
	
	/**
	 * 用户认证通过后跳转搜索查询页面
	 * @return
	 */
	@RequestMapping(value={"/query"}, method=RequestMethod.GET)
	public String getQuery(HttpServletRequest request) {
		
		if(securityService.findLoggedUsernameAndAuth() != null) {
			HttpSession session = request.getSession();
			String userAndAuth = securityService.findLoggedUsernameAndAuth();
			session.setAttribute("user", userAndAuth.substring(0, userAndAuth.indexOf("/")));
		}
		return "query";
	}
	
	/**
	 * 用户认证通过后跳转批量下载页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/batch"}, method=RequestMethod.GET)
	public String getBatch(HttpServletRequest request) {
		
		if(securityService.findLoggedUsernameAndAuth() != null) {
			HttpSession session = request.getSession();
			String userAndAuth = securityService.findLoggedUsernameAndAuth();
			session.setAttribute("user", userAndAuth.substring(0, userAndAuth.indexOf("/")));
		}
		return "batch";
	}
	
	/**
	 * 登陆遇到error时返回错误提示页面
	 * @return
	 */
	@RequestMapping(value={"/403"}, method= RequestMethod.GET)
	public String getError() {
		
		return "error";
	}

}
