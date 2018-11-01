package com.github.bioinfo.webes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="gene_pmid", type="gene_pmid_list")
public class Pmid2GeneEntity {

	@Id
	private int _id;
	private String pmid;
	
	@Field(type=FieldType.String, index=FieldIndex.not_analyzed)
	private String gene_id;
	private String mentions;
	private String resource;
	
	@Field(type=FieldType.String, index=FieldIndex.not_analyzed)
	private String symbol;
	private String alias_symbol;
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getGene_id() {
		return gene_id;
	}
	public void setGene_id(String gene_id) {
		this.gene_id = gene_id;
	}
	public String getMentions() {
		return mentions;
	}
	public void setMentions(String mentions) {
		this.mentions = mentions;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getAlias_symbol() {
		return alias_symbol;
	}
	public void setAlias_symbol(String alias_symbol) {
		this.alias_symbol = alias_symbol;
	}
}
