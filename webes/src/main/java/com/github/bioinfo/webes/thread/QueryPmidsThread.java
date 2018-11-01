package com.github.bioinfo.webes.thread;

import java.util.concurrent.CountDownLatch;
import com.github.bioinfo.webes.dao.Var2PmidRepository;
import com.github.bioinfo.webes.model.TotalNumAndPmidsModel;
import com.github.bioinfo.webes.service.RedisService;

public class QueryPmidsThread implements Runnable {
	
	private String var;
	private Var2PmidRepository repository;
	private RedisService redisService;
	private TotalNumAndPmidsModel model;
	private int flag;
	private CountDownLatch countDownLatch;
	/**
	 * 
	 * @param repository
	 * @param redis
	 * @param result
	 * @param flag 1-var; 2-gene; 3-disease; 4-chemical
	 */
	public QueryPmidsThread(Var2PmidRepository repository, RedisService redis, TotalNumAndPmidsModel model, CountDownLatch countDownLatch, String var, int flag) {
		this.flag = flag;
		this.repository = repository;
		this.redisService = redis;
		this.model = model;
		this.countDownLatch = countDownLatch;
		this.var = var;
	}
	@Override
	public void run() {
		try {
			
			if(1==flag) {
				//�ȴ�redis�ж��Ƿ����
				if(redisService.existPmidsFromRedis("var" + var)) {
					model.setModel(redisService.getModelFromRedis("var" + var));
				} else { //�����ES�в�ѯ
					model.setModel(repository.getPmidsByVar(var.trim()));
					
					if(model.getNum()<10000) {
						//numС��10000����Сʱ������redis
						redisService.insertPmids2Redis("var" + var, model);
					} else {
						//num����10000���־û�������redis
						redisService.insertPmids2RedisWithoutTime("var" + var, model);
					}
				}
			}
			
			if(2== flag) {
				//�ȴ�redis�ж��Ƿ����
				if(redisService.existPmidsFromRedis("gene" + var)) {
					model.setModel(redisService.getModelFromRedis("gene" + var));
				} else { //�����ES�в�ѯ
					model.setModel(repository.getPmidsByGene(var.trim()));
					if(model.getNum()<10000) {
						//numС��10000����Сʱ������redis
						redisService.insertPmids2Redis("gene" + var, model);
					} else {
						//num����10000���־û�������redis
						redisService.insertPmids2RedisWithoutTime("gene" + var, model);
					}
				}
			}
			
			if(3== flag) {
				//�ȴ�redis�ж��Ƿ����
				if(redisService.existPmidsFromRedis("disease" + var)) {
					model.setModel(redisService.getModelFromRedis("disease" + var));
				} else { //�����ES�в�ѯ
					model.setModel(repository.getPmidsByDisease(var.trim()));
					if(model.getNum()<10000) {
						//numС��10000����Сʱ������redis
						redisService.insertPmids2Redis("disease" + var, model);
					} else {
						//num����10000���־û�������redis
						redisService.insertPmids2RedisWithoutTime("disease" + var, model);
					}
				}
			}
			
			if(4 == flag) {
				//�ȴ�redis�ж��Ƿ����
				if(redisService.existPmidsFromRedis("chemical" + var.trim())) {
					model.setModel(redisService.getModelFromRedis("chemical" + var.trim()));
				} else { //�����ES�в�ѯ
					model.setModel(repository.getPmidsByChemical(var.trim()));
					if(model.getNum()<10000) {
						//numС��10000����Сʱ������redis
						redisService.insertPmids2Redis("chemical" + var, model);
					} else {
						//num����10000���־û�������redis
						redisService.insertPmids2RedisWithoutTime("chemical" + var, model);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			countDownLatch.countDown();
		}
		
	}

}
