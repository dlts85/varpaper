package com.github.bioinfo.webes.dao;

import java.util.Map;

public interface Pmid2TitleRepository {

	public Map<String, Object> getTitleAbstractByPmid(String varParam, String gene, String disease, String chemical, Integer rows, Integer page, String ifRange,
			String dateRange, String modelSelect);
	
	public Map<String, Object> getBatchTitleByVar(String varParam, String gene, String disease, String chemical, String ifRange,
			String dateRange, String modelSelect);

}
