package com.github.bioinfo.webes.entity;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(indexName="pmid_title", type="pmid_title_list")
public class SimpleTitleInfoEntity {

	@Id
	private String pmid;
	private String title;
	private String context;
	private String pmcid;
	private String journal;
	private String IF;
	private String casCategory;
	private String casIndex;
	private Date pubDate;
	private String issn;
	
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
	
	public String getPmcid() {
		return pmcid;
	}
	public void setPmcid(String pmcid) {
		this.pmcid = pmcid;
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
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
