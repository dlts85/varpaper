package com.github.bioinfo.webes.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

public class TotalNumAndPmidsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long num;
	private LinkedHashMap<String, Set<String>> pmid2Var = new LinkedHashMap<>();
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public LinkedHashMap<String, Set<String>> getPmid2Var() {
		return pmid2Var;
	}
	public void setPmid2Var(LinkedHashMap<String, Set<String>> pmid2Var) {
		this.pmid2Var = pmid2Var;
	}
	
	public void setModel(TotalNumAndPmidsModel model) {
		this.num = model.getNum();
		this.pmid2Var = model.getPmid2Var();
	}
}
