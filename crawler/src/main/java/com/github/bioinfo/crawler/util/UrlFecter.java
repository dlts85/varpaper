package com.github.bioinfo.crawler.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.bioinfo.crawler.entity.Pmid2TitleDetailEntity;
import com.github.bioinfo.crawler.entity.Pmid2TitleEntity;
import com.github.bioinfo.crawler.parse.NCBIParse;

/**
 * 根据url
 * “https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?
 * db=pubmed&id=11748933,11700088&retmode=xml”
 * 获取待插入数据库的实体
 * @author fujian
 *
 */
public class UrlFecter {
	
	final static Logger logger = LoggerFactory.getLogger(UrlFecter.class);

	//final static RequestConfig param = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
	
	public static List<Pmid2TitleEntity> urlParser(HttpClient client, String url) throws Exception {
		
		List<Pmid2TitleEntity> entitys = new ArrayList<Pmid2TitleEntity>();
		
		//HttpResponse response = HttpUtils.getXML(client, url);
		HttpGet getMethod = new HttpGet(url);
		//getMethod.setConfig(param);
		//getMethod.addHeader(":authority", "eutils.ncbi.nlm.nih.gov");
		//getMethod.addHeader(":method", "GET");
		//getMethod.addHeader(":scheme", "https");
		getMethod.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		getMethod.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		getMethod.addHeader("accept-encoding", "gzip, deflate, br");
		getMethod.addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
		
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		
		try {
			
			response = client.execute(getMethod);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("statusCode is " + statusCode);
			if(200 == statusCode) {
				String xmlContent = EntityUtils.toString(response.getEntity());
				entitys = NCBIParse.getTitleInfo(xmlContent);
			}
			return entitys;
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
			
		} finally {
			getMethod.abort();
			EntityUtils.consumeQuietly(response.getEntity());
		}
	}
	
public static List<Pmid2TitleDetailEntity> urlParser4TitleDetail(HttpClient client, String url) throws Exception {
		
		List<Pmid2TitleDetailEntity> entitys = new ArrayList<Pmid2TitleDetailEntity>();
		
		//HttpResponse response = HttpUtils.getXML(client, url);
		HttpGet getMethod = new HttpGet(url);
		//getMethod.setConfig(param);
		//getMethod.addHeader(":authority", "eutils.ncbi.nlm.nih.gov");
		//getMethod.addHeader(":method", "GET");
		//getMethod.addHeader(":scheme", "https");
		getMethod.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		getMethod.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		getMethod.addHeader("accept-encoding", "gzip, deflate, br");
		getMethod.addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
		
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		
		try {
			
			response = client.execute(getMethod);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode is " + statusCode);
			if(200 == statusCode) {
				String xmlContent = EntityUtils.toString(response.getEntity());
				entitys = NCBIParse.getTitleDetail(xmlContent);
			}
			return entitys;
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			throw new RuntimeException(e);
			
		} finally {
			getMethod.abort();
			EntityUtils.consumeQuietly(response.getEntity());
		}
	}
}
