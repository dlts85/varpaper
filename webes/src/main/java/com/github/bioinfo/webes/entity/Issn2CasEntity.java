package com.github.bioinfo.webes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="issn_cas", type="issn_cas_list")
public class Issn2CasEntity {
	
	@Id
	private int _id;
	private String full_title;
	private String abbre_title;
	private String issn;
	private String cas_category;
	private String cas_index;
	private String ifactor;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int id) {
		this._id = id;
	}
	
	public String getFull_title() {
		return full_title;
	}
	public void setFull_title(String fullTitle) {
		this.full_title = fullTitle;
	}
	
	public String getAbbre_title() {
		return abbre_title;
	}
	public void setAbbre_title(String abbreTitle) {
		this.abbre_title = abbreTitle;
	}
	
	public String getIssn() {
		return issn;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	
	public String getCas_category() {
		return cas_category;
	}
	public void setCas_category(String casCategory) {
		this.cas_category = casCategory;
	}
	
	public String getCas_index() {
		return cas_index;
	}
	public void setCas_index(String casIndex) {
		this.cas_index = casIndex;
	}
	
	public String getIfactor() {
		return ifactor;
	}
	public void setIfactor(String ifactor) {
		this.ifactor = ifactor;
	}

}
