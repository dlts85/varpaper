/*package com.github.bioinfo.webes.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import com.github.bioinfo.crawler.entity.Pmid2TitleEntity;
import com.github.bioinfo.crawler.util.HttpClientObject;
import com.github.bioinfo.crawler.util.UrlFecter;

public class GetPubmedTitleInfoCrawler {

	*//**
	 * ����pmid ��ȡ���¸�Ҫ��Ϣ
	 * @param pmids
	 * @return
	 *//*
	public static List<Pmid2TitleEntity> getTitleInfoByPmids(List<String> pmids) {
		
		List<Pmid2TitleEntity> entities = new ArrayList<>();
		
		String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=";

		int count = 1;
		for(String pmid : pmids) { //������ȡ
			url += pmid + ",";
			
			if(count % 100 == 0) { //100��pmidΪһ�� ��ȡ
				
				try {
					url = url.substring(0, url.lastIndexOf(","));
					url += "&retmode=xml";
					
					HttpClient client = HttpClientObject.getHttpClient();
					
					System.out.println("url is " + url);
					List<Pmid2TitleEntity> list = UrlFecter.urlParser(client, url);
					System.out.println(list.size() + " documents has crawled");
					entities.addAll(list);
					//Thread.sleep(500);
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=";
				}
			}
			count++;
		}
		
		return entities;
	}
}
*/