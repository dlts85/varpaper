package com.github.bioinfo.webes.aop;

import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.bioinfo.webes.dao.RedisDao;
import com.github.bioinfo.webes.service.SecurityService;

@Component
@Aspect
public class BioInfoAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(BioInfoAspect.class);

	@Autowired
	SecurityService securityService;
	
	@Autowired
	RedisDao redis;
	
	@Pointcut("execution(* com.github.bioinfo.webes.dao.Pmid2TitleRepoImp.getTitleAbstractByPmid(..))")
	public void pointCut() {
	};
	
	@Pointcut("execution(* com.github.bioinfo.webes.controller.Var2TitleController.getBatchTitleInfoByVar(..))")
	public void batchPointCut() {
	};
	
	
	@Around("pointCut()")
	public Object getTitleAbstract(ProceedingJoinPoint jp) throws Throwable {
		
		String userAndAuth = securityService.findLoggedUsernameAndAuth();
		String userId = userAndAuth.substring(0, userAndAuth.lastIndexOf("/"));

		if("EMPOLYEE".equals(userAndAuth.substring(userAndAuth.lastIndexOf("/")+1))) { //一般个人用户
			//判断个人用户查询次数是否达到最大值
			if(redis.exists(userId)){
				int count = Integer.parseInt(redis.get(userId, "count").toString());
				if(count<15) {
					redis.incrBy(userId, "count", 1);
				} else {
					Map<String, Object> result = new HashMap<>();
					result.put("flag", "limit15");
					return result;
				}
			} else {
				Map<String, Object> count = new HashMap<>();
				count.put("count", "1");
				redis.setMap(userId, count, 86400);
			}	
		}
		
		
		//查询记录写日志
		String argsStr = "";
		Object[] args = jp.getArgs();
		for(Object arg : args) {
			argsStr += (arg.toString() + ",");
		}
		
		logger.info("user " + userId + "  's query params are " + argsStr);
		
		return jp.proceed();
		
	}
	
	@Around("batchPointCut()")
	public Object isEnterprise(ProceedingJoinPoint jp) throws Throwable {
		String userAndAuth = securityService.findLoggedUsernameAndAuth();
		String userId = userAndAuth.substring(0, userAndAuth.lastIndexOf("/"));

		if("ENTERPRISE".equals(userAndAuth.substring(userAndAuth.lastIndexOf("/")+1))) { //企业用户
			//判断企业用户查询次数是否达到最大值
			if(redis.exists(userId)){
				int count = Integer.parseInt(redis.get(userId, "count").toString());
				if(count<15) {
					redis.incrBy(userId, "count", 1);
				} else {
					Map<String, Object> result = new HashMap<>();
					result.put("flag", "limit15");
					return result;				}
			} else {
				Map<String, Object> count = new HashMap<>();
				count.put("count", "1");
				redis.setMap(userId, count, 86400);
			}
			
			//查询记录写日志
			String argsStr = "";
			Object[] args = jp.getArgs();
			for(Object arg : args) {
				argsStr += (arg.toString() + ",");
			}
			
			logger.info("batch user " + userId + "  's query params are " + argsStr);
			
			return jp.proceed();
			
		} else {
			Map<String, Object> result = new HashMap<>();
			result.put("flag", "not enterprise");
			return result;
		}
	}
	
}

