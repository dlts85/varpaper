package com.github.bioinfo.webes.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.bioinfo.webes.entity.Pmid2ChemicalEntity;
import com.github.bioinfo.webes.entity.Pmid2DiseaseEntity;
import com.github.bioinfo.webes.entity.Pmid2GeneEntity;
import com.github.bioinfo.webes.entity.Var2PmidEntity;
import com.github.bioinfo.webes.model.TotalNumAndEntities;
import com.github.bioinfo.webes.model.TotalNumAndPmidsModel;
import com.github.bioinfo.webes.utils.EsScrollUtil;

@Repository
public class Var2PmidRepoImp implements Var2PmidRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(Pmid2TitleRepoImp.class);

	@Autowired
	EsScrollUtil esUtil;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TotalNumAndPmidsModel getPmidsByVar(String var) {
		
		LinkedHashMap<String, Set<String>> pmid2vars = new LinkedHashMap<>();
				
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder
			.should(QueryBuilders.matchPhraseQuery("paper", var))
			.should(QueryBuilders.matchPhraseQuery("snpeff_ann", var))
			.should(QueryBuilders.matchPhraseQuery("chr_id", var))
			.should(QueryBuilders.matchPhraseQuery("snp", var))
			.should(QueryBuilders.matchPhraseQuery("var_s", var))
			.should(QueryBuilders.matchPhraseQuery("var", var))
			.should(QueryBuilders.matchPhraseQuery("mutation_id", var))
			.should(QueryBuilders.matchPhraseQuery("cdna", var))
			.should(QueryBuilders.matchPhraseQuery("clinvarID", var));
		
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(boolQueryBuilder)
				.withIndices("var_pmid")
				.withTypes("var_pmid_list")
				.build();
		
		TotalNumAndEntities<Var2PmidEntity> entities2 = esUtil.queryNAndTotalNum(searchQuery2, 100000, Var2PmidEntity.class);
		
		if(entities2 != null) {
			String pmid = "";
			String snpeff = "";
			UnaryOperator<String> unaryOperator = single -> single.replaceAll("  ", " ");
			UnaryOperator<String> unaryOperator2 = single -> single.replaceAll("[c\\.$|p\\.$|]", "");
			
			for(Var2PmidEntity entity : entities2.getEntites()) {
				pmid = entity.getPmid();
				if(null ==pmid2vars.get(pmid)) {
					pmid2vars.put(pmid, new HashSet<>());
				}
				pmid2vars.get(pmid).add(entity.getPaper().replaceAll("  ", " "));
				pmid2vars.get(pmid).add(entity.getCdna());
				pmid2vars.get(pmid).add(entity.getCdna().replaceAll("c.", ""));
				pmid2vars.get(pmid).add(entity.getVar().replaceAll("[c\\.$|p\\.$|]", ""));
				pmid2vars.get(pmid).add(entity.getVar_s().replaceAll("[c\\.$|p\\.$|]", ""));
				snpeff = entity.getSnpeff_ann();
				if(null != snpeff && !"".equals(snpeff)) {
					List<String> snpeffList = Arrays.asList(snpeff.split(","));
					snpeffList.replaceAll(unaryOperator);
					pmid2vars.get(pmid).addAll(snpeffList);
					List<String> snpeffList2 = snpeffList;
					snpeffList2.replaceAll(unaryOperator2);
					pmid2vars.get(pmid).addAll(snpeffList2);
				}
			}
		}
		
		TotalNumAndPmidsModel model = new TotalNumAndPmidsModel();
		model.setNum(entities2.getNum());
		model.setPmid2Var(pmid2vars);
		return model;
	}

	@Override
	public TotalNumAndPmidsModel getPmidsByGene(String gene) {
		
		
		LinkedHashMap<String, Set<String>> pmid2Genes = new LinkedHashMap<>();
		
		TotalNumAndEntities<Pmid2GeneEntity> entities = new TotalNumAndEntities<>();
		
		List<String> geneList = Arrays.asList(gene.split(";"));
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for(String gen : geneList) {
			
			boolQueryBuilder
			.should(QueryBuilders.queryStringQuery(gen).field("symbol"))
			.should(QueryBuilders.matchPhraseQuery("mentions", gen))
			.should(QueryBuilders.queryStringQuery(gen).field("gene_id"));
			
		}		
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(boolQueryBuilder)
				.withIndices("gene_pmid")
				.withTypes("gene_pmid_list")
				.build();
		
		entities = esUtil.queryNAndTotalNum(searchQuery, 100000, Pmid2GeneEntity.class);

		
		if(!entities.isEmpty()) {
			String pmid = "";
			String mentions = "";
			String alias_symbol = "";
			UnaryOperator<String> unaryOperator = single -> single = (" " + single.trim() + " ");
			for(Pmid2GeneEntity entity : entities.getEntites()) {
				pmid = entity.getPmid();
				if(null == pmid2Genes.get(pmid)) {
					pmid2Genes.put(pmid, new HashSet<>());
				}
				
				pmid2Genes.get(pmid).add(" "+entity.getGene_id().trim()+" ");
				pmid2Genes.get(pmid).add(" "+entity.getSymbol().trim()+" ");
				
				mentions = entity.getMentions();
				if(null != mentions && !"".equals(mentions)) {
					List<String> mentionsList = Arrays.asList(mentions.split("\\|"));
					mentionsList.replaceAll(unaryOperator);
					pmid2Genes.get(pmid).addAll(mentionsList);
				}
				alias_symbol = entity.getAlias_symbol();
				if(null != alias_symbol && !"".equals(alias_symbol)) {
					List<String> aliasSymList = Arrays.asList(alias_symbol.split("\\|"));
					aliasSymList.replaceAll(unaryOperator);
					pmid2Genes.get(pmid).addAll(aliasSymList);
				}
			}
		}
		
		TotalNumAndPmidsModel model = new TotalNumAndPmidsModel();
		model.setNum(entities.getNum());
		model.setPmid2Var(pmid2Genes);
		return model;
	}
	
	/**
	 * 多种表型以‘；’分割，求各自的pmid，然后取pmid的交集
	 */
	@Override
	public TotalNumAndPmidsModel getPmidsByDisease(String disease) {
		
		LinkedHashMap<String, Set<String>> pmid2Disease = new LinkedHashMap<>();
				
		UnaryOperator<String> unaryOperator1 = single -> single.trim();
		List<String> diseaseList = Arrays.asList(disease.split(";"));
		diseaseList.replaceAll(unaryOperator1);
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for(String dis : diseaseList) {
			
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			if("1234567890".contains(dis.substring(0, 1))) {
				boolQueryBuilder2
				.should(QueryBuilders.wildcardQuery("source", dis + "*"))
				.should(QueryBuilders.wildcardQuery("disease", dis + "*"));
			} else {
				boolQueryBuilder2
				.should(QueryBuilders.matchPhraseQuery("source", dis))
				.should(QueryBuilders.matchPhraseQuery("disease", dis));
			}
			
			boolQueryBuilder.must(boolQueryBuilder2);
		}
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(boolQueryBuilder)
				.withIndices("disease_mesh")
				.withTypes("disease_mesh_list")
				.withFields("meshId")
				.build();
		
		logger.info("query first pmid from disease start in " + new Date());
		List<String> firstPage = esUtil.queryAllKey(searchQuery, "meshId");
		
		logger.info("query first pmid from disease end in " + new Date());
		
		TotalNumAndEntities<Pmid2DiseaseEntity> totalEntity = new TotalNumAndEntities<>();
		if(!firstPage.isEmpty()) {
			
			/*UnaryOperator<String> unaryOperator = single -> single.replaceAll(" '", "");
			unaryOperator.andThen(single -> single.replaceAll("'", ""));
			unaryOperator.andThen(single -> single = ("\\s" + single + "\\s"));*/
			
			Set<String> meshIds = new HashSet<>();
			meshIds.addAll(firstPage);
			/*for(String entity : firstPage) {
				meshIds.add(entity.getMeshId());
				
				String diseases = entity.getDisease();
				String[] diseasess = diseases.split(",");
				
				highLights.addAll(Arrays.asList(diseasess));
				highLights.replaceAll(unaryOperator);
			}*/
			
			logger.info("meshIds include " + meshIds.size());
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			for(String meshId : meshIds) {
				boolQueryBuilder2.should(QueryBuilders.matchPhraseQuery("mesh_id", meshId));
			}			
			SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
					.withQuery(boolQueryBuilder2)
					.withIndices("disease_pmid")
					.withTypes("disease_pmid_list")
					.build();
			
			logger.info("query pmids from disease start in " + new Date());

			//返回最多十万条记录及总记录数
			totalEntity = esUtil.queryNAndTotalNum(searchQuery2, 100000, Pmid2DiseaseEntity.class);
			
			logger.info("match title num is" + totalEntity.getEntites().size());
			logger.info("query pmids from disease end in " + new Date());

		}
		
		logger.info("begin get highlight in" + new Date());
		if(!totalEntity.isEmpty()) {
			String pmid = "";
			String mentions = "";
			String mesh_name = "";
			//UnaryOperator<String> unaryOperator = single -> single.replaceAll(" ", "");
			
			UnaryOperator<String> unaryOperator = single -> single = (" " + single + " ");

			//boolean needHighLight = true;
			for(Pmid2DiseaseEntity entity : totalEntity.getEntites()) {
				
				pmid = entity.getPmid();
				if(null == pmid2Disease.get(pmid)) {
					pmid2Disease.put(pmid, new HashSet<>());
				}
				
				pmid2Disease.get(pmid).add(entity.getMesh_id());
				
				mentions = entity.getMentions();
				if(null != mentions && !"".equals(mentions)) {
					List<String> mentionsList = Arrays.asList(mentions.split("\\|"));
					mentionsList.replaceAll(unaryOperator);
					pmid2Disease.get(pmid).addAll(mentionsList);
				}
				mesh_name = entity.getMesh_name();
				if(null != mesh_name && !"".equals(mesh_name)) {
					List<String> meshNameList = Arrays.asList(mesh_name.split(","));
					meshNameList.replaceAll(unaryOperator);
					pmid2Disease.get(pmid).addAll(meshNameList);
				}
				
				/*if(needHighLight) {
					
										
					if(pmid2Disease.size()>=10000) {
						needHighLight = false;
					}
				}*/
			}
		}
		
		logger.info("end get highlight in " + new Date());
		
		TotalNumAndPmidsModel model = new TotalNumAndPmidsModel();
		model.setNum(totalEntity.getNum());
		model.setPmid2Var(pmid2Disease);
		return model;
		
	}

	@Override
	public TotalNumAndPmidsModel getPmidsByChemical(String chemical) {
		
		LinkedHashMap<String, Set<String>> pmid2Chemical = new LinkedHashMap<>();
		
		TotalNumAndEntities<Pmid2ChemicalEntity> entities = new TotalNumAndEntities<>();
				
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder
			.should(QueryBuilders.matchPhraseQuery("source", chemical))
			.should(QueryBuilders.matchPhraseQuery("chemical", chemical));
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(boolQueryBuilder)
				.withIndices("chemical_mesh")
				.withTypes("chemical_mesh_list")
				.withFields("meshId")
				.build();
		
		List<String> firstPage = esUtil.queryAllKey(searchQuery, "meshId");		
		Set<String> meshIds = new HashSet<>(firstPage);
				
		if(!firstPage.isEmpty()) {
			
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			for(String meshId : meshIds) {
				boolQueryBuilder2.should(QueryBuilders.matchPhraseQuery("mesh_id", meshId));
			}
			SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
					.withQuery(boolQueryBuilder2)
					.withIndices("chemcial_pmid")
					.withTypes("chemical_pmid_list")
					.build();

			entities = esUtil.queryNAndTotalNum(searchQuery2, 100000, Pmid2ChemicalEntity.class);
			
		}
		
		if(!entities.isEmpty()) {
			UnaryOperator<String> unaryOperator3 = single -> single= " "+single+" ";
			String pmid = "";			
			for(Pmid2ChemicalEntity entity : entities.getEntites()) {
				pmid = entity.getPmid();
				if(null == pmid2Chemical.get(pmid)) {
					pmid2Chemical.put(pmid, new HashSet<>());
				}
				
				String mentions = entity.getMentions();
				if(null != mentions && !"".equals(mentions)) {
					List<String> mentionsList = Arrays.asList(mentions.split("\\|"));
					mentionsList.replaceAll(unaryOperator3);
					pmid2Chemical.get(pmid).addAll(mentionsList);
				}
				
				pmid2Chemical.get(pmid).add(" " +entity.getMesh_name().trim()+" ");
			}
		}
		
		TotalNumAndPmidsModel model = new TotalNumAndPmidsModel();
		model.setNum(entities.getNum());
		model.setPmid2Var(pmid2Chemical);
		return model;
		
	}

	/*@Override
	public Set<String> getVarSetByPmidAndVar(String pmid, String var) {
		
		Set<String> result = new HashSet<>();
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.termQuery("pmid", pmid))
				.withIndices("var_pmid")
				.withTypes("var_pmid_list")
				.build();
		
		List<Var2PmidEntity> entities = esUtil.queryAll(searchQuery, Var2PmidEntity.class);
		
		String snpeff = "";
		for(Var2PmidEntity entity : entities) {
			if(var.equals(entity.getPaper()) || var.equals(entity.getSnp()) || var.equals(entity.getVar_s()) || var.equals(entity.getVar())|| entity.getSnpeff_ann().contains(var) || entity.getChr_id().contains(var)) {
				result.add(var);
				result.add(entity.getPaper());
				snpeff = entity.getSnpeff_ann();
				if(null != snpeff && !"".equals(snpeff)) {
					List<String> snpeffList = Arrays.asList(snpeff.split(","));
					result.addAll(snpeffList);
				}
			}
		}
		
		return result;
		
	}
*/
}
