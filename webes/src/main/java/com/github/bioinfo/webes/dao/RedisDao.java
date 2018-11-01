package com.github.bioinfo.webes.dao;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * redis 缓存操作
 * @author fujian
 *
 */
public interface RedisDao {
	
	/*批量删除对应的value*/
	public void remove(String... keys);
	
	/*批量删除key*/
	public void removePattern(String pattern);
	
	/*删除对应的value*/
	public void remove(String key);
	
	/*删除map中的key-value*/
	public void remove(String key, String field);
	
	/*判断缓存中是否有对应的value*/
	public boolean exists(String key);
	
	public boolean exists(String key, String field);
	
	/*读取缓存*/
	public Map<Object, Object> getMap(String key);
	
	/**
	 * 读取缓存
	 * @param key
	 * @return
	 */
	public Object get(String key);
	
	public Object get(String key, String field);
	
	/**
	 * 写入缓存
	 * @param key
	 * @param model
	 * @return
	 */
	public boolean set(String key, LinkedHashMap<String, Set<String>> model);
	
	/**
	 * 写入缓存
	 * @param key
	 * @param model
	 * @return
	 */
	public boolean set(String key, Object model, long expireTime);
	
	public boolean set(String key, Object model);

	
	public boolean setMap(String key, Map<String, Object> value, long expireTime);
	
	/*以一个梯度增加*/
	public void incrBy(String key, String field, int nm);	
}
