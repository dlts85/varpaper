package com.github.bioinfo.webes.service;

import java.util.Map;

public interface Pmid2TitleService {
	
	/**
	 * ����������졢���������������������ķ�ҳ���¸�Ҫ�б�
	 * @author fujian
	 * @param var
	 * @param rows
	 * @param page
	 * @param ifRange
	 * @param dateRange
	 * @param modelSelect
	 * @return
	 */
	public Map<String, Object> getTitleAbstractByPmid(String var, String gene, String disease, String chemical, Integer rows, Integer page, String ifRange,
			String dateRange, String modelSelect);
	
	/**
	 * ����������졢�������������������¸�Ҫ�ļ�
	 * @param var
	 * @param gene
	 * @param disease
	 * @param chemical
	 * @param ifRange
	 * @param dateRange
	 * @return
	 */
	public Map<String, Object> getBatchTitleByVar(String var, String gene, String disease, String chemical, String ifRange, String dateRange, String modelSelect);

}
