package com.github.bioinfo.webes.service;

import java.util.Set;

import com.github.bioinfo.webes.model.TotalNumAndPmidsModel;

public interface RedisService {

	public boolean insertPmids2Redis(String varId, TotalNumAndPmidsModel model);
	
	public boolean insertPmids2RedisWithoutTime(String varId, TotalNumAndPmidsModel model);
	
	public boolean existPmidsFromRedis(String varId);
	
	public TotalNumAndPmidsModel getModelFromRedis(String varId);
	
	public Set<String> getPmidsFromRedis(String varId);
	
	public void deletePmids(String varId);
}
