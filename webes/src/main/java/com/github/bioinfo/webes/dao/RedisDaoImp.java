package com.github.bioinfo.webes.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDaoImp implements RedisDao {

	@Autowired
	private RedisTemplate<Serializable, Object> redisTemplate;
	
	@Override
	public void remove(String... keys) {
		for(String key : keys) {
			remove(key);
		}
	}

	@Override
	public void removePattern(String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if(keys.size()>0)
			redisTemplate.delete(keys);
	}

	@Override
	public void remove(String key) {
		if(exists(key)) {
			redisTemplate.delete(key);
		}
	}

	@Override
	public void remove(String key, String field) {
		BoundHashOperations<Serializable, Object, Object> operations = redisTemplate.boundHashOps(key);
		operations.delete(field);
	}

	@Override
	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	@Override
	public boolean exists(String key, String field) {
		return redisTemplate.opsForHash().hasKey(key, field);
	}

	@Override
	public Map<Object, Object> getMap(String key) {
		Map<Object, Object> result = null;
		HashOperations<Serializable, Object, Object> operations = redisTemplate.opsForHash();
		result = operations.entries(key);
		return result;
	}
	
	@Override
	public Object get(String key) {
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		return operations.get(key);
	}

	@Override
	public boolean set(String key, LinkedHashMap<String, Set<String>> value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean set(String key, Object value, long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public boolean set(String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	@Override
	public void incrBy(String key, String field, int nm) {
		BoundHashOperations<Serializable, Object,Object> operations =  redisTemplate.boundHashOps(key);
		operations.increment(field, nm);	
	}
	
	@Override
	public Object get(String key, String field) {
		HashOperations<Serializable, Object,Object> operations =  redisTemplate.opsForHash();
		return operations.get(key, field);
	}

	@Override
	public boolean setMap(String key, Map<String, Object> value, long expireTime) {
		boolean result = false;
		try {
			HashOperations<Serializable, Object, Object> operations = redisTemplate.opsForHash();
			operations.putAll(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
