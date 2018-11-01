package com.github.bioinfo.webes.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import com.github.bioinfo.crawler.entity.Pmid2TitleEntity;
import com.github.bioinfo.webes.entity.SimpleTitleInfoEntity;
import com.github.bioinfo.webes.model.TotalNumAndEntities;
import com.github.bioinfo.webes.model.TotalNumAndPmidsModel;
import com.github.bioinfo.webes.service.RedisService;
import com.github.bioinfo.webes.thread.QueryPmidsThread;
import com.github.bioinfo.webes.utils.CommonUtil;
import com.github.bioinfo.webes.utils.EsScrollUtil;

@Repository
public class Pmid2TitleRepoImp implements Pmid2TitleRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(Pmid2TitleRepoImp.class);
	
	@Autowired
	EsScrollUtil esUtil;
	
	@Autowired
	private Var2PmidRepository var2PmidRepo;
	
	@Autowired
	RedisService redisService;
	
	private Executor executor = Executors.newCachedThreadPool();

	
	@Override
	public Map<String, Object> getTitleAbstractByPmid(String varParam, String gene, String disease, String chemical, Integer rows, Integer page, String ifRange,
			String dateRange, String modelSelect) {
		
		//一个请求中的多模块处理以多线程方式处理
		int count = 0;
		
		Map<String, Object> result = new HashMap<>();
		List<Pmid2TitleEntity> titleList = new ArrayList<>();
		result.put("count", 0);
		result.put("titleList", titleList);
		
		if(CommonUtil.isEmpty(varParam) && CommonUtil.isEmpty(gene) && CommonUtil.isEmpty(disease) && CommonUtil.isEmpty(chemical)) {
			return result;
		} else {
			if(!CommonUtil.isEmpty(varParam)) {
				count++;
			}
			if(!CommonUtil.isEmpty(gene)) {
				count++;
			}
			if(!CommonUtil.isEmpty(disease)) {
				count++;
			}
			if(!CommonUtil.isEmpty(chemical)) {
				count++;
			}
		}
		//翻页到100页，不再进行
		//能翻到第几页，可以根据user的标识去决定
		//终止页可以返回特殊标识诸如100.1 前端根据该标识将翻页动作禁止
		if(page == 101) {
			page = 100;
		}
		
		CountDownLatch countDownLatch = new CountDownLatch(count); //各模块各占独自线程处理
		
		
		TotalNumAndPmidsModel varModel = new TotalNumAndPmidsModel();
		TotalNumAndPmidsModel geneModel = new TotalNumAndPmidsModel();
		TotalNumAndPmidsModel disModel = new TotalNumAndPmidsModel();
		TotalNumAndPmidsModel chemicalModel = new TotalNumAndPmidsModel();
		
		List<String> inputParams = new ArrayList<>(); //用户输入切词后作为待高亮
		UnaryOperator<String> unaryOperator = single -> single = (" " + single.trim() + " ");
		
		try {
			//变异输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(varParam)) {
				
				List<String> varList = Arrays.asList(varParam.split("\\s+"));
				if(isSingle(varList)) {
					inputParams.add(" " + varParam.trim() + " ");
				} else {
					varList.replaceAll(unaryOperator);
					inputParams.addAll(varList);
				}
				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, varModel, countDownLatch, varParam, 1));
			}
			//基因输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(gene)) {
				
				List<String> varList = Arrays.asList(gene.split(";"));
				varList.replaceAll(unaryOperator);
				inputParams.addAll(varList);
				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, geneModel, countDownLatch, gene, 2));
			}
			//疾病输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(disease)) {
				
				List<String> multi =  Arrays.asList(disease.split(";"));
				multi.replaceAll(unaryOperator);
				inputParams.addAll(multi);

				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, disModel, countDownLatch, disease, 3));
			}
			//药物输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(chemical)) {
				
				List<String> varList = Arrays.asList(chemical.split("\\s+"));
				if(isSingle(varList)) {
					inputParams.add(" " + chemical.trim() + " ");
				} else {
					varList.replaceAll(unaryOperator);
					inputParams.addAll(varList);
				}
				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, chemicalModel, countDownLatch, chemical, 4));
			}
			
			countDownLatch.await();
			
		} catch (Exception e) {
			logger.error("four model query pmids occur error owing to " + e.getMessage());
		}
			
		logger.info("cross begin in " + new Date());
		//四者取交集
		Set<String> varSet = varModel.getPmid2Var().keySet();
		Set<String> geneSet = geneModel.getPmid2Var().keySet();
		Set<String> diseaseSet = disModel.getPmid2Var().keySet();
		Set<String> chemicalSet = chemicalModel.getPmid2Var().keySet();
		
		Set<Set<String>> allSet = new LinkedHashSet<>();
		
		TotalNumAndPmidsModel existModel = new TotalNumAndPmidsModel();
		if(!CommonUtil.isEmpty(varParam)) {
			allSet.add(varSet);
			existModel = varModel;
		}
		if(!CommonUtil.isEmpty(gene)) {
			allSet.add(geneSet);
			existModel = geneModel;
		}
		if(!CommonUtil.isEmpty(chemical)) {
			allSet.add(chemicalSet);
			existModel = chemicalModel;
		}
		if(!CommonUtil.isEmpty(disease)) {
			allSet.add(diseaseSet);
			existModel = disModel;
		}
				
		@SuppressWarnings("unchecked")
		Set<String> crossSet = allSet.stream()
			.reduce((set1, set2) -> {
				set1.retainAll(set2);
				return set1;
			})
			.orElse(Collections.EMPTY_SET);				
		
		//对pmids进行ifactor及pubDate过滤
		
		logger.info("restricted pmids num is " + crossSet.size());
		
		logger.info("cross end in " + new Date());
		
		//开始查询
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		
		//加过滤条件
		if(!"all".equals(ifRange)) { //if filter
			//String[] ifStr = ifRange.split("-");
			booleanQueryBuilder
				.filter(QueryBuilders.rangeQuery("IF").gte(Float.parseFloat(ifRange.trim())));
				//.filter(QueryBuilders.rangeQuery("IF").lte(Float.parseFloat(ifStr[1])));
		}
		if(!"all".equals(dateRange)) { //date range filter
			String[] dateStr = dateRange.split("-");
			String beginDate = dateStr[0] + "-01-01";
			String endDate = dateStr[1] + "-12-31";
			
			booleanQueryBuilder
			.filter(QueryBuilders.rangeQuery("pubDate").gte(CommonUtil.convertStrDate2Date(beginDate, null)))
			.filter(QueryBuilders.rangeQuery("pubDate").lte(CommonUtil.convertStrDate2Date(endDate, null)));
		}
		
		//模块过滤
		if(!"all".equals(modelSelect)) { //model filter
			
			String varName = "";
			if("var".equals(modelSelect)) {
				varName = "var_pmids";
			}
			if("gene".equals(modelSelect)) {
				varName = "gene_pmids";
			}
			if("disease".equals(modelSelect)) {
				varName = "disease_pmids";
			}
			if("chemical".equals(modelSelect)) {
				varName = "chemical_pmids";
			}
			
			logger.info("take all pmids from redis begin " + new Date());
			
			Set<String> modelPmids = ModelPmidsSingleton.getModelSet(modelSelect);
			if(0 == modelPmids.size()) {
				modelPmids = redisService.getPmidsFromRedis(varName);
				ModelPmidsSingleton.setModel(modelPmids, modelSelect);
			}
			logger.info("take all pmids from redis end and begin retain " + new Date());
			
			crossSet.retainAll(modelPmids);
			logger.info("retain end " + new Date());
		}
		
		booleanQueryBuilder
		.must(QueryBuilders.idsQuery().addIds(crossSet));
		
		//分页查询
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(booleanQueryBuilder)
				.withIndices("pmid_title")
				.withTypes("pmid_title_list")
				.withPageable(new PageRequest(page-1, rows))
				.build();
		
		logger.info("pageable start in " + new Date());

		//titleList =  esUtil.queryForList(searchQuery2, Pmid2TitleEntity.class);
		AggregatedPage<Pmid2TitleEntity> pageable = esUtil.queryForPage(searchQuery2, Pmid2TitleEntity.class);
		
		titleList = pageable.getContent();//该页的内容
		
		if(allSet.size() ==0 || allSet.size() >1 || !"all".equals(ifRange) || !"all".equals(dateRange) || !"all".equals(modelSelect)) {//总记录数
			result.put("count", pageable.getTotalElements());
		} else {
			result.put("count", existModel.getNum());
		}
				
		logger.info("pageable end in " + new Date());

		logger.info("highlighe start in " + new Date());
		//给列表里每一篇文章概要添加摘要需要高亮展示的字段
		//String context = "";
		for(Pmid2TitleEntity entity : titleList) {
			List<String> var2Hight = new ArrayList<>();
			
			var2Hight.addAll(inputParams);
			
			Set<String> varsByVar = varModel.getPmid2Var().get(entity.getPmid());
			/*if(null != varsByVar) {
				var2Hight.addAll(varsByVar);

			}*/
			Set<String> varsByGene = geneModel.getPmid2Var().get(entity.getPmid());
			if(null != varsByGene) {
				var2Hight.addAll(varsByGene);

			}
			Set<String> varsByDis = disModel.getPmid2Var().get(entity.getPmid());
			if(null != varsByDis) {
				var2Hight.addAll(varsByDis);

			}
			Set<String> varsByChemical = chemicalModel.getPmid2Var().get(entity.getPmid());
			if(null != varsByChemical) {
				var2Hight.addAll(varsByChemical);

			}
			entity.setHighLight(var2Hight);
			if(null != varsByVar) {
				entity.getHighLight().addAll(varsByVar);
			}
			
			//给列表里每一篇文章概要添加正文需要高亮展示的字段
			String pmcid = entity.getPmcid();
			
			if(!"".equals(pmcid) && null != pmcid) {
				
				pmcid = pmcid.replace("PMC", "");
				
				List<String> contextHight = new ArrayList<>();
				
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
				if(varsByVar != null) { //假如存在待高亮变异字段，正文里必须优先高亮变异
					for(String param : varsByVar) {
						boolQueryBuilder2.should(QueryBuilders.matchPhraseQuery("content.paras", " " +param + " "));
					}					
					boolQueryBuilder.must(boolQueryBuilder2);
					boolQueryBuilder.must(QueryBuilders.termQuery("pmcid", pmcid));
					contextHight = esUtil.getHightLight("pmid_title_detail", "pmid_title_detail_list", "content.paras", boolQueryBuilder, 3);
					entity.setLightContext(contextHight);
				}
				if(entity.getLightContext() == null) {
					entity.setLightContext(new ArrayList<String>());
				}
				int existSize = entity.getLightContext().size();
				if(existSize <3) {
					for(String param : var2Hight) {
						boolQueryBuilder2.should(QueryBuilders.matchPhraseQuery("content.paras", " " +param + " "));
					}
					boolQueryBuilder.must(boolQueryBuilder2);
					boolQueryBuilder.must(QueryBuilders.termQuery("pmcid", pmcid));
					
					contextHight = esUtil.getHightLight("pmid_title_detail", "pmid_title_detail_list", "content.paras", boolQueryBuilder, 3-existSize);
					
					entity.getLightContext().addAll(contextHight);
				}
			} else {
				entity.setLightContext(new ArrayList<String>());
			}
		}
		
		logger.info("highlighe end in " + new Date());


		result.put("titleList", titleList);
		return result;
	}

	/**
	 * 批量下载文件
	 */
	@Override
	public Map<String, Object> getBatchTitleByVar(String varParam, String gene, String disease, String chemical,
			String ifRange, String dateRange, String modelSelect) {
		
		//一个请求中的多模块处理以多线程方式处理
		int count = 0;
		
		Map<String, Object> result = new HashMap<>();
		List<SimpleTitleInfoEntity> titleList = new ArrayList<>();
		result.put("count", 0);
		result.put("titleList", titleList);
		
		if(CommonUtil.isEmpty(varParam) && CommonUtil.isEmpty(gene) && CommonUtil.isEmpty(disease) && CommonUtil.isEmpty(chemical)) {
			return result;
		} else {
			if(!CommonUtil.isEmpty(varParam)) {
				count++;
			}
			if(!CommonUtil.isEmpty(gene)) {
				count++;
			}
			if(!CommonUtil.isEmpty(disease)) {
				count++;
			}
			if(!CommonUtil.isEmpty(chemical)) {
				count++;
			}
		}
		
		CountDownLatch countDownLatch = new CountDownLatch(count); //各模块各占独自线程处理
		
		
		TotalNumAndPmidsModel varModel = new TotalNumAndPmidsModel();
		TotalNumAndPmidsModel geneModel = new TotalNumAndPmidsModel();
		TotalNumAndPmidsModel disModel = new TotalNumAndPmidsModel();
		TotalNumAndPmidsModel chemicalModel = new TotalNumAndPmidsModel();
		
		List<String> inputParams = new ArrayList<>(); //用户输入切词后作为待高亮
		try {
			//变异输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(varParam)) {
				
				List<String> varList = Arrays.asList(varParam.split("\\s+"));
				if(isSingle(varList)) {
					inputParams.add(varParam);
				} else {
					inputParams.addAll(varList);
				}
				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, varModel, countDownLatch, varParam, 1));
			}
			//基因输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(gene)) {
				
				List<String> varList = Arrays.asList(gene.split("\\s+"));
				if(isSingle(varList)) {
					inputParams.add(gene);
				} else {
					inputParams.addAll(varList);
				}
				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, geneModel, countDownLatch, gene, 2));
			}
			//疾病输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(disease)) {
				
				List<String> multi =  Arrays.asList(disease.split(";"));
				inputParams.addAll(multi);

				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, disModel, countDownLatch, disease, 3));
			}
			//药物输入 返回pmid 2 highLight part
			if(!CommonUtil.isEmpty(chemical)) {
				
				List<String> varList = Arrays.asList(chemical.split("\\s+"));
				if(isSingle(varList)) {
					inputParams.add(chemical);
				} else {
					inputParams.addAll(varList);
				}
				
				executor.execute(new QueryPmidsThread(var2PmidRepo, redisService, chemicalModel, countDownLatch, chemical, 4));
			}
			
			countDownLatch.await();
			
		} catch (Exception e) {
			logger.error("four model query pmids occur error owing to " + e.getMessage());
		}
			
		logger.info("cross begin in " + new Date());
		//四者取交集
		Set<String> varSet = varModel.getPmid2Var().keySet();
		Set<String> geneSet = geneModel.getPmid2Var().keySet();
		Set<String> diseaseSet = disModel.getPmid2Var().keySet();
		Set<String> chemicalSet = chemicalModel.getPmid2Var().keySet();
		
		Set<Set<String>> allSet = new LinkedHashSet<>();
				
		if(!varSet.isEmpty()) {
			allSet.add(varSet);
		}
		if(!geneSet.isEmpty()) {
			allSet.add(geneSet);
		}
		if(!chemicalSet.isEmpty()) {
			allSet.add(chemicalSet);
		}
		if(!diseaseSet.isEmpty()) {
			allSet.add(diseaseSet);
		}
		
		@SuppressWarnings("unchecked")
		Set<String> crossSet = allSet.stream()
			.reduce((set1, set2) -> {
				set1.retainAll(set2);
				return set1;
			})
			.orElse(Collections.EMPTY_SET);				
				
		logger.info("restricted pmids num is " + crossSet.size());
		
		logger.info("cross end in " + new Date());
		
		//开始查询
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		booleanQueryBuilder
			.must(QueryBuilders.idsQuery().addIds(crossSet));
		
		//加过滤条件
		if(!"all".equals(ifRange)) { //if filter
			//String[] ifStr = ifRange.split("-");
			booleanQueryBuilder
				.filter(QueryBuilders.rangeQuery("IF").gte(Float.parseFloat(ifRange.trim())));
				//.filter(QueryBuilders.rangeQuery("IF").lte(Float.parseFloat(ifStr[1])));
		}
		if(!"all".equals(dateRange)) { //date range filter
			String[] dateStr = dateRange.split("-");
			String beginDate = dateStr[0] + "-01-01";
			String endDate = dateStr[1] + "-12-31";
			
			booleanQueryBuilder
			.filter(QueryBuilders.rangeQuery("pubDate").gte(CommonUtil.convertStrDate2Date(beginDate, null)))
			.filter(QueryBuilders.rangeQuery("pubDate").lte(CommonUtil.convertStrDate2Date(endDate, null)));
		}
		
		//模块过滤
		if(!"all".equals(modelSelect)) { //model filter
			
			String varName = "";
			if("var".equals(modelSelect)) {
				varName = "var_pmids";
			}
			if("gene".equals(modelSelect)) {
				varName = "gene_pmids";
			}
			if("disease".equals(modelSelect)) {
				varName = "disease_pmids";
			}
			if("chemical".equals(modelSelect)) {
				varName = "chemical_pmids";
			}
			
			logger.info("take all pmids from redis begin " + new Date());
			
			Set<String> modelPmids = ModelPmidsSingleton.getModelSet(modelSelect);
			if(0 == modelPmids.size()) {
				modelPmids = redisService.getPmidsFromRedis(varName);
				ModelPmidsSingleton.setModel(modelPmids, modelSelect);
			}
			logger.info("take all pmids from redis end and begin retain " + new Date());
			
			crossSet.retainAll(modelPmids);
			logger.info("retain end " + new Date());
		}
		
		booleanQueryBuilder
		.must(QueryBuilders.idsQuery().addIds(crossSet));
		
		//分页查询
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(booleanQueryBuilder)
				.withIndices("pmid_title")
				.withTypes("pmid_title_list")
				.build();
		
		//titleList =  esUtil.queryForList(searchQuery2, Pmid2TitleEntity.class);
		//批量只取前10000条document
		TotalNumAndEntities<SimpleTitleInfoEntity> totoal = esUtil.queryNAndTotalNum(searchQuery2, 10000, SimpleTitleInfoEntity.class);
		
		titleList = totoal.getEntites();//所有的实体
		
		result.put("count", totoal.getNum());

		result.put("titleList", titleList);
		return result;

	}
	
    public boolean isSingle(List<String> list) {
		
		for(String single : list) {
			if(single.length() == 1) {
				return true;
			}
		}
		return false;
	}
}
