package com.github.bioinfo.crawler.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(indexName="pmid_title", type="pmid_title_list")
public class Pmid2TitleEntity {

	@Id
	private String pmid;
	private String title;
	private String doi;
	private String pmcid;
	private String authorAddress;
	private String journal;
	private String IF;
	private String casCategory;
	private String casIndex;
	private String context;
	private Date pubDate;
	private String issn;
	@Transient
	private List<String> highLight = new ArrayList<String>();
	
	@Transient
	private List<String> lightContext = new ArrayList<String>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	
	public String getPmcid() {
		return pmcid;
	}
	public void setPmcid(String pmcid) {
		this.pmcid = pmcid;
	}
	
	public String getAuthorAddress() {
		return authorAddress;
	}
	public void setAuthorAddress(String authorAddress) {
		this.authorAddress = authorAddress;
	}
	
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	
	@JsonProperty(value="IF")
	public String getIF() {
		return IF;
	}
	public void setIF(String ifactor) {
		this.IF = ifactor;
	}
	
	public String getCasCategory() {
		return casCategory;
	}
	public void setCasCategory(String casCategory) {
		this.casCategory = casCategory;
	}
	
	public String getCasIndex() {
		return casIndex;
	}
	public void setCasIndex(String casIndex) {
		this.casIndex = casIndex;
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	public Date getPubDate() {
		return pubDate;
	}
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	
	public String getIssn() {
		return issn;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	
	public List<String> getHighLight() {
		return highLight;
	}
	public void setHighLight(List<String> highLight) {
		this.highLight = highLight;
	}
	public List<String> getLightContext() {
		return lightContext;
	}
	public void setLightContext(List<String> lightContext) {
		this.lightContext = lightContext;
	}
	
}
