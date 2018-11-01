package com.github.bioinfo.webes.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import com.github.bioinfo.crawler.entity.Pmid2TitleDetailEntity;
import com.github.bioinfo.crawler.util.HttpClientObject;
import com.github.bioinfo.crawler.util.UrlFecter;

public class CrawlerAndSaveTitleDetail  implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(CrawlerAndSaveTitleDetail.class);
	private List<String> pmcids;
	private ElasticsearchTemplate template;
	
	
	public CrawlerAndSaveTitleDetail(List<String> pmcids, ElasticsearchTemplate template) {
		this.pmcids = pmcids;
		this.template = template;
		
	}

	@Override
	public void run() {
				
		int count = 1;
		List<IndexQuery> queries = new ArrayList<>();		
		String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pmc&id=";
		for(String pmid : pmcids) { //分批爬取
			url += pmid + ",";
			
			if(count % 20 == 0) { //20个pmcid为一组 爬取
				
				try {
					url = url.substring(0, url.lastIndexOf(","));
					url += "&tool=my_tool&email=linfujian1999@sina.com";
					
					HttpClient client = HttpClientObject.getHttpClient();
					
					logger.info("url is " + url);
					List<Pmid2TitleDetailEntity> wait2InsertEntity = UrlFecter.urlParser4TitleDetail(client, url);
					logger.info(wait2InsertEntity.size() + " documents has crawled");
				
					for(Pmid2TitleDetailEntity entity : wait2InsertEntity) { //新增文章信息到db
						
						//为防止一次性爬取文章概要太多，分批导入es
						IndexQuery indexQuery = new IndexQuery();
						indexQuery.setId(entity.getPmcid()+ "");
						indexQuery.setObject(entity);
						indexQuery.setIndexName("pmid_title_detail");
						indexQuery.setType("pmid_title_detail_list");
						
						queries.add(indexQuery);
						
					}
					
					template.bulkIndex(queries);
					logger.info("bulkIndex counter : " + queries.size());
					queries.clear();
							
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pmc&id=";
				}
			}
			count++;
		}
		
		if(url.contains(",")) {
			
			try {
				url = url.substring(0, url.lastIndexOf(","));
				url += "&retmode=xml";
				
				HttpClient client = HttpClientObject.getHttpClient();
				
				System.out.println("url is " + url);
				List<Pmid2TitleDetailEntity> wait2InsertEntity = UrlFecter.urlParser4TitleDetail(client, url);
				System.out.println(wait2InsertEntity.size() + " documents has crawled");
			
				for(Pmid2TitleDetailEntity entity : wait2InsertEntity) { //新增文章信息到db
					
					//为防止一次性爬取文章概要太多，分批导入es
					IndexQuery indexQuery = new IndexQuery();
					indexQuery.setId(entity.getPmcid()+ "");
					indexQuery.setObject(entity);
					indexQuery.setIndexName("pmid_title_detail");
					indexQuery.setType("pmid_title_detail_list");
					
					queries.add(indexQuery);
					
				}
				
				template.bulkIndex(queries);
				System.out.println("bulkIndex counter : " + queries.size());
				queries.clear();
						
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		System.out.println("bulkIndex completed.");

	}

}
