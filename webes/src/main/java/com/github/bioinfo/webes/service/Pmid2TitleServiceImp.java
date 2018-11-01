package com.github.bioinfo.webes.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.bioinfo.webes.dao.Pmid2TitleRepository;

@Service
public class Pmid2TitleServiceImp implements Pmid2TitleService {
	
	@Autowired
	Pmid2TitleRepository repository;
	
	@Override
	public Map<String, Object> getTitleAbstractByPmid(String var, String gene, String disease, String chemical, Integer rows, Integer page, String ifRange,
			String dateRange, String modelSelect) {
		
		return repository.getTitleAbstractByPmid(var, gene, disease, chemical, rows, page, ifRange, dateRange, modelSelect);
	}

	@Override
	public Map<String, Object> getBatchTitleByVar(String var, String gene, String disease, String chemical,
			String ifRange, String dateRange, String modelSelect) {
		
		Map<String, Object> tmp = repository.getBatchTitleByVar(var, gene, disease, chemical, ifRange, dateRange, modelSelect);
		
		return tmp;
	}
}
