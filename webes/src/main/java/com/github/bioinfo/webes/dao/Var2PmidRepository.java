package com.github.bioinfo.webes.dao;

import com.github.bioinfo.webes.model.TotalNumAndPmidsModel;

public interface Var2PmidRepository {

	/**
	 * 根据变异获取pmid2highLight
	 * @param var
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByVar(String var);
	
	/**
	 * 根据pmid var 获取 var2pmid 中的 所有变异
	 * @param pmid
	 * @return
	 */
	//public Set<String> getVarSetByPmidAndVar(String pmid, String varParam);
	
	/**
	 * 根据gene获取pmid2highLight
	 * @param gene
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByGene(String gene);
	
	/**
	 * 根据疾病获取pmid2highLight
	 * @param disease
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByDisease(String disease);
	
	/**
	 * 根据药物获取pmid2highLight
	 * @param chemical
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByChemical(String chemical);
}
