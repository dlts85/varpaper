package com.github.bioinfo.webes.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 根据用户输入返回的所有的pmids2highlight
 * @author fujian
 *
 */
public class Pmids2VarSetModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private LinkedHashMap<String, Set<String>> pmid2VarsList;
	
	public LinkedHashMap<String, Set<String>> getPmid2Vars() {
		return pmid2VarsList;
	}
	public void setPmid2Vars(LinkedHashMap<String, Set<String>> pmid2VarsList) {
		this.pmid2VarsList = pmid2VarsList;
	}
}
