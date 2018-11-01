package com.github.bioinfo.webes.dao;

import com.github.bioinfo.webes.model.TotalNumAndPmidsModel;

public interface Var2PmidRepository {

	/**
	 * ���ݱ����ȡpmid2highLight
	 * @param var
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByVar(String var);
	
	/**
	 * ����pmid var ��ȡ var2pmid �е� ���б���
	 * @param pmid
	 * @return
	 */
	//public Set<String> getVarSetByPmidAndVar(String pmid, String varParam);
	
	/**
	 * ����gene��ȡpmid2highLight
	 * @param gene
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByGene(String gene);
	
	/**
	 * ���ݼ�����ȡpmid2highLight
	 * @param disease
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByDisease(String disease);
	
	/**
	 * ����ҩ���ȡpmid2highLight
	 * @param chemical
	 * @return
	 */
	public TotalNumAndPmidsModel getPmidsByChemical(String chemical);
}
