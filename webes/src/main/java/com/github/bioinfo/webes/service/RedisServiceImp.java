package com.github.bioinfo.webes.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.bioinfo.webes.dao.RedisDao;
import com.github.bioinfo.webes.model.TotalNumAndPmidsModel;

@Service
public class RedisServiceImp implements RedisService {

	@Autowired
	RedisDao redisDao;
	
	@Override
	public boolean insertPmids2Redis(String varId, TotalNumAndPmidsModel model) {
		
		return redisDao.set(varId, model, 1800l);
		
	}
	
	@Override
	public boolean insertPmids2RedisWithoutTime(String varId, TotalNumAndPmidsModel model) {
		return redisDao.set(varId, model);
	}

	@Override
	public TotalNumAndPmidsModel getModelFromRedis(String varId) {
		Object model = redisDao.get(varId);
		if(model instanceof TotalNumAndPmidsModel) {
			return (TotalNumAndPmidsModel)model;
		}
		return null;
	}

	@Override
	public void deletePmids(String key) {
		redisDao.remove(key);
		
	}

	@Override
	public boolean existPmidsFromRedis(String varId) {
		return redisDao.exists(varId);
	}

	@Override
	public Set<String> getPmidsFromRedis(String varId) {
		Object set = redisDao.get(varId); 
		if(set instanceof Set) {
			return (Set<String>) set;
		}
		return null;
	}

}
