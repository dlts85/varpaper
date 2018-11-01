package com.github.bioinfo.webes.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import com.github.bioinfo.crawler.entity.Pmid2TitleEntity;
import com.github.bioinfo.crawler.util.HttpClientObject;
import com.github.bioinfo.crawler.util.UrlFecter;
import com.github.bioinfo.webes.entity.Issn2CasEntity;

public class CrawlerAndSaveTitleUtil  implements Runnable {

	private List<String> pmids;
	private ElasticsearchTemplate template;
	
	
	public CrawlerAndSaveTitleUtil(List<String> pmids, ElasticsearchTemplate template) {
		this.pmids = pmids;
		this.template = template;
		
	}

	@Override
	public void run() {
				
		String issn = "";
		int count = 1;
		List<IndexQuery> queries = new ArrayList<>();		
		String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=";
		for(String pmid : pmids) { //������ȡ
			url += pmid + ",";
			
			if(count % 100 == 0) { //100��pmidΪһ�� ��ȡ
				
				try {
					url = url.substring(0, url.lastIndexOf(","));
					url += "&retmode=xml";
					
					HttpClient client = HttpClientObject.getHttpClient();
					
					System.out.println("url is " + url);
					List<Pmid2TitleEntity> wait2InsertEntity = UrlFecter.urlParser(client, url);
					System.out.println(wait2InsertEntity.size() + " documents has crawled");
				
					for(Pmid2TitleEntity entity : wait2InsertEntity) { //�������¸�Ҫ��Ϣ��db
						
						//saveǰ��ifactor��cas_category��cas_index set into entity ͨ��issn����
						issn = entity.getIssn();
						
						SearchQuery searchQuery3 = new NativeSearchQueryBuilder()
								.withQuery(QueryBuilders.matchPhraseQuery("issn", issn))
								.build();
						
						List<Issn2CasEntity> issnList = template.queryForList(searchQuery3, Issn2CasEntity.class);
						if(1 == issnList.size()) {
							Issn2CasEntity entity2 = issnList.get(0);
							entity.setIF(entity2.getIfactor());
							entity.setCasCategory(entity2.getCas_category());
							entity.setCasIndex(entity2.getCas_index());
						}
						
						//Ϊ��ֹһ������ȡ���¸�Ҫ̫�࣬��������es
						IndexQuery indexQuery = new IndexQuery();
						indexQuery.setId(entity.getPmid()+ "");
						indexQuery.setObject(entity);
						indexQuery.setIndexName("pmid_title");
						indexQuery.setType("pmid_title_list");
						
						queries.add(indexQuery);
						
					}
					
					template.bulkIndex(queries);
					System.out.println("bulkIndex counter : " + queries.size());
					queries.clear();
							
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=";
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
				List<Pmid2TitleEntity> wait2InsertEntity = UrlFecter.urlParser(client, url);
				System.out.println(wait2InsertEntity.size() + " documents has crawled");
			
				for(Pmid2TitleEntity entity : wait2InsertEntity) { //�������¸�Ҫ��Ϣ��db
					
					//saveǰ��ifactor��cas_category��cas_index set into entity ͨ��issn����
					issn = entity.getIssn();
					
					SearchQuery searchQuery3 = new NativeSearchQueryBuilder()
							.withQuery(QueryBuilders.matchPhraseQuery("issn", issn))
							.build();
					
					List<Issn2CasEntity> issnList = template.queryForList(searchQuery3, Issn2CasEntity.class);
					if(1 == issnList.size()) {
						Issn2CasEntity entity2 = issnList.get(0);
						entity.setIF(entity2.getIfactor());
						entity.setCasCategory(entity2.getCas_category());
						entity.setCasIndex(entity2.getCas_index());
					}
					
					//Ϊ��ֹһ������ȡ���¸�Ҫ̫�࣬��������es
					IndexQuery indexQuery = new IndexQuery();
					indexQuery.setId(entity.getPmid()+ "");
					indexQuery.setObject(entity);
					indexQuery.setIndexName("pmid_title");
					indexQuery.setType("pmid_title_list");
					
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
