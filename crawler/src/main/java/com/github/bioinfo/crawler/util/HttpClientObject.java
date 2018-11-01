package com.github.bioinfo.crawler.util;

import java.io.IOException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * 获取httpClient的单例模式
 * @author fujian
 *
 */
public class HttpClientObject {
	

	private static CloseableHttpClient httpClient;
	
	static {
		
		try {	
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null,  new TrustSelfSignedStrategy());
			//不进行主机名验证
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", new PlainConnectionSocketFactory())
					.register("https", sslConnectionSocketFactory)
					.build();
			
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(200);
			
			httpClient = HttpClients.custom()
					.setSSLSocketFactory(sslConnectionSocketFactory)
					.setDefaultCookieStore(new BasicCookieStore())
					.setConnectionManager(cm)
					.setRetryHandler(new HttpRequestRetryHandler() {
						
						public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
							if(executionCount > 3) {
								System.out.println("Maximum tries reached for client http pool");
								return false;
							}
							if(exception instanceof NoHttpResponseException) {
								System.out.println("No response from server on " + executionCount + " call");
								return true;
							}
							return false;
						}
					})
					.build();
			
		} catch (Exception e) {
			e.printStackTrace();
			httpClient = HttpClients.createDefault();
		}
		
	}
	/**
	 * 获取池连接的最大连接数为200的httpclient
	 * @return
	 */
	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}

}
