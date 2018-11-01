/*package com.github.bioinfo.webes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.http.client.HttpClient;
import com.github.bioinfo.crawler.entity.Pmid2TitleEntity;
import com.github.bioinfo.crawler.util.HttpClientObject;
import com.github.bioinfo.crawler.util.UrlFecter;

public class CrawlerThread  implements Runnable {

	private CountDownLatch countDownLatch;
	private List<String> input;
	private List<Pmid2TitleEntity> result = new ArrayList<>();
	private String url;
	
	
	public CrawlerThread(CountDownLatch countDownLatch, List<String> input, String url) {
		this.countDownLatch = countDownLatch;
		this.input = input;
		this.url = url;
		
	}
	@Override
	public void run() {
		try {
			int count = 1;
			for(String pmid : input) { //分批爬取
				url += pmid + ",";
				
				if(count % 100 == 0) { //100个pmid为一组 爬取
					
					try {
						url = url.substring(0, url.lastIndexOf(","));
						url += "&retmode=xml";
						
						HttpClient client = HttpClientObject.getHttpClient();
						
						System.out.println("url is " + url);
						List<Pmid2TitleEntity> list = UrlFecter.urlParser(client, url);
						result.addAll(list);
						System.out.println(list.size() + " documents has crawled");
						//Thread.sleep(1000);
						
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=";
					}
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Thread.currentThread().getName() + "has account a error owing to " + e.getMessage());
		} finally {
			System.out.println(Thread.currentThread().getName() + " has finished crawlered");
			countDownLatch.countDown();
		}
	}
	
	public List<Pmid2TitleEntity> getList() {
		return this.result;
	}

}
*/