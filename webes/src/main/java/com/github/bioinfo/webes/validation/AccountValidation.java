package com.github.bioinfo.webes.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.github.bioinfo.webes.entity.Account;
import com.github.bioinfo.webes.service.AccountService;

@Component
public class AccountValidation implements Validator {

	@Autowired
	AccountService accountService;
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Account.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Account account = (Account) o;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.accountForm.name");
		if(account.getUserName().length() < 6 || account.getUserName().length() > 32) {
			errors.rejectValue("userName", "Size.accountForm.username");
		}
		if(accountService.findByUserName(account.getUserName()) != null) {
			errors.rejectValue("userName", "Duplicate.accountForm.username");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.accountForm.password");
		if(account.getPassword().length() < 8 || account.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.accountForm.password");
		}
		
		if(!account.getPasswordConfirm().equals(account.getPassword())) {
			errors.rejectValue("passwordConfirm", "Diff.accountForm.passwordConfirm");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telphone", "NotEmpty.accountForm.telphone");
		if(account.getTelphone().length() < 1 || account.getTelphone().length() > 12) {
			errors.rejectValue("telphone", "Size.accountForm.telphone");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.accountForm.email");
		if(account.getEmail().length() < 1 || account.getEmail().length() > 32) {
			errors.rejectValue("email", "Size.accountForm.email");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "researchPoint", "NotEmpty.accountForm.researchPoint");
		if(account.getEmail().length() < 1 || account.getEmail().length() > 32) {
			errors.rejectValue("researchPoint", "Size.accountForm.researchPoint");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company", "NotEmpty.accountForm.company");
		if(account.getEmail().length() < 1 || account.getEmail().length() > 32) {
			errors.rejectValue("company", "Size.accountForm.company");
		}
		
	}

}
