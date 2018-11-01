package com.github.bioinfo.webes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="account", type="account_list")
public class Account {
	
	public static final String ROLE_MANAGER = "MANAGER";
	public static final String ROLE_EMPLOYEE= "EMPOLYEE";
	
	@Id
	private String userName;
	private String password;
	private String passwordConfirm;
	private String telphone;
	private String email;
	private String researchPoint;
	private String company;
	private String demand;
	private boolean active;
	private String userRole;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String username) {
		this.userName = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordconfirm) {
		this.passwordConfirm = passwordconfirm;
	}
	
	public boolean isActive(){
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "[" + this.userName + "," + this.password + "," + this.userRole + "]";
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getResearchPoint() {
		return researchPoint;
	}
	public void setResearchPoint(String researchPoint) {
		this.researchPoint = researchPoint;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDemand() {
		return demand;
	}
	public void setDemand(String demand) {
		this.demand = demand;
	}
}
