package com.github.bioinfo.webes.dao;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * redis �������
 * @author fujian
 *
 */
public interface RedisDao {
	
	/*����ɾ����Ӧ��value*/
	public void remove(String... keys);
	
	/*����ɾ��key*/
	public void removePattern(String pattern);
	
	/*ɾ����Ӧ��value*/
	public void remove(String key);
	
	/*ɾ��map�е�key-value*/
	public void remove(String key, String field);
	
	/*�жϻ������Ƿ��ж�Ӧ��value*/
	public boolean exists(String key);
	
	public boolean exists(String key, String field);
	
	/*��ȡ����*/
	public Map<Object, Object> getMap(String key);
	
	/**
	 * ��ȡ����
	 * @param key
	 * @return
	 */
	public Object get(String key);
	
	public Object get(String key, String field);
	
	/**
	 * д�뻺��
	 * @param key
	 * @param model
	 * @return
	 */
	public boolean set(String key, LinkedHashMap<String, Set<String>> model);
	
	/**
	 * д�뻺��
	 * @param key
	 * @param model
	 * @return
	 */
	public boolean set(String key, Object model, long expireTime);
	
	public boolean set(String key, Object model);

	
	public boolean setMap(String key, Map<String, Object> value, long expireTime);
	
	/*��һ���ݶ�����*/
	public void incrBy(String key, String field, int nm);	
}
