/*package com.github.bioinfo.webes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.github.bioinfo.crawler.entity.Pmid2TitleEntity;
import com.google.common.collect.Lists;

*//**
 * ��ȡpubmed������Ϣ������
 * @author fujian
 *
 *//*
public class GetPubmedTitleInfoCrawlerMultiThread {

	*//**
	 * ����pmid ��ȡ���¸�Ҫ��Ϣ
	 * @param pmids
	 * @return
	 *//*
	public static List<Pmid2TitleEntity> getTitleInfoByPmids(List<String> pmids) {
		
		List<Pmid2TitleEntity> entitys = new ArrayList<Pmid2TitleEntity>();
		List<CrawlerThread> referenceList = new ArrayList<>();
		try {
			
			String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=";
			
			int interval = 100;
			
			List<List<String>> partList =  Lists.partition(pmids, interval);
			
			CountDownLatch countDownLatch = new CountDownLatch(partList.size());
			Executor executor = Executors.newCachedThreadPool();

			for(int i = 0; i<partList.size(); i++) {
				
				CrawlerThread thread = new CrawlerThread(countDownLatch, partList.get(i), url);
				executor.execute(thread);
				
				referenceList.add(thread);
			}
			
			countDownLatch.await(); //���������߳���ȡ���ǰ���̱߳�������
			
			for(CrawlerThread single : referenceList) {
				List<Pmid2TitleEntity> rs = single.getList();
				if(null!= rs) {
					entitys.addAll(rs);
				}
			}
			
			return entitys;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entitys;
	}
	
	public static void main(String[] args) {
		
		List<Student> list = new ArrayList<>();

		for(int i=0; i<3; i++) {
			Student student = new Student();
			list.add(student);
			student.set(i+"");
		}
		
		for(int i = 0; i<3; i++) {
			String syString = list.get(i).get();
			System.out.println(syString);
		}
		
	}
}
*/