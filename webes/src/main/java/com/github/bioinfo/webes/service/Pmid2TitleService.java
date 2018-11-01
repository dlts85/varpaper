package com.github.bioinfo.webes.service;

import java.util.Map;

public interface Pmid2TitleService {
	
	/**
	 * 根据输入变异、过滤条件返回满足条件的分页文章概要列表
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
	 * 根据输入变异、过滤条件批量返回文章概要文件
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
