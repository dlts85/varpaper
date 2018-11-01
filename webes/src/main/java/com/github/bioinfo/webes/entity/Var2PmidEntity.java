package com.github.bioinfo.webes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="var_pmid", type="var_pmid_list")
public class Var2PmidEntity {

	@Id
	private int _id;
	private String pmid;
	private String nlp;
	private String paper;
	private String resource;
	private String kinds;
	private String snp;
	private String var;
	private String var_s;
	private String mutation_id;
	private String chr_id;
	private String snpeff_ann;
	private String cdna;
	private String gene;

	public int get_id() {
		return _id;
	}
	public void set_id(int id) {
		this._id = id;
	}

	public String getNlp() {
		return nlp;
	}
	public void setNlp(String nlp) {
		this.nlp = nlp;
	}

	public String getPaper() {
		return paper;
	}
	public void setPaper(String paper) {
		this.paper = paper;
	}

	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getKinds() {
		return kinds;
	}
	public void setKinds(String kinds) {
		this.kinds = kinds;
	}

	public String getSnp() {
		return snp;
	}
	public void setSnp(String snp) {
		this.snp = snp;
	}

	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}

	public String getVar_s() {
		return var_s;
	}
	public void setVar_s(String var_s) {
		this.var_s = var_s;
	}

	public String getChr_id() {
		return chr_id;
	}
	public void setChr_id(String chr_id) {
		this.chr_id = chr_id;
	}

	public String getSnpeff_ann() {
		return snpeff_ann;
	}
	public void setSnpeff_ann(String snpeff_ann) {
		this.snpeff_ann = snpeff_ann;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getMutation_id() {
		return mutation_id;
	}
	public void setMutation_id(String mutation_id) {
		this.mutation_id = mutation_id;
	}
	public String getCdna() {
		return cdna;
	}
	public void setCdna(String cdna) {
		this.cdna = cdna;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	
}
