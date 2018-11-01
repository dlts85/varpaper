package com.github.bioinfo.crawler.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="pmid_title_detail", type="pmid_title_detail_list")
public class Pmid2TitleDetailEntity {
	
	@Id
	private String pmcid;
	private String pmid;
	private String title;
	
	@Field(type = FieldType.Nested)
	private List<TitleSectionEntity> content;

	public String getPmcid() {
		return pmcid;
	}

	public void setPmcid(String pmcid) {
		this.pmcid = pmcid;
	}

	public List<TitleSectionEntity> getContent() {
		return content;
	}

	public void setContent(List<TitleSectionEntity> content) {
		this.content = content;
	}

	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
