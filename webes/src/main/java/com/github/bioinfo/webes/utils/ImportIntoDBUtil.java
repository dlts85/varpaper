package com.github.bioinfo.webes.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.bioinfo.webes.dao.Pmid2TitleRepoImp;
import com.github.bioinfo.webes.dao.RedisDao;
import com.github.bioinfo.webes.entity.Account;

@Controller
public class ImportIntoDBUtil extends ElasticsearchTemplate {
	
	public ImportIntoDBUtil(Client client) {
		super(client);
	}
	
	private static final Logger logger = LoggerFactory.getLogger(Pmid2TitleRepoImp.class);
	
	@Autowired
	EsScrollUtil esUtil;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RedisDao redis;
	
	@RequestMapping(value={"/import/var2pmid/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String importvar2Pmid2Es(@RequestParam("path") String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf("/")) + "/var2pmid.json");
		
		int num = 15539892;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split("\\t", 12);
				
				result.append("{\"index\":{\"_index\":\"var_pmid\",\"_type\":\"var_pmid_list\",\"_id\":" + num + "}}\r\n");
				result.append("{\"mutation_id\":\"" + lineArray[0] +  "\",\"pmid\":\"" + lineArray[1] + "\",\"chr_id\":\""+ lineArray[2] + "\",\"cdna\":\""+ lineArray[3] + "\",\"clinvarID\": \"" + lineArray[4] + "\",\"gene\":\"" + lineArray[5] + "\",\"paper\":\""+lineArray[6]+"\",\"resource\":\""+lineArray[7]+"\",\"snp\":\""+lineArray[8]+"\",\"snpeff_ann\":\""+lineArray[9]+"\",\"var\":\""+lineArray[10]+"\",\"var_s\":\""+lineArray[11] +"\"}\r\n");
			
				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + num + "owing to " + e.getMessage();
		}
		return "success convert";
		
	}
	
	
	@RequestMapping(value={"/import/issn2cas/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String importIssn2Cas(@RequestParam("path") String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf("/")) + "/issn2cas.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split(",");
				
				result.append("{\"index\":{\"_index\":\"issn_cas\",\"_type\":\"issn_cas_list\",\"_id\":" + num + "}}\r\n");
				result.append("{\"full_title\":\"" + lineArray[1] + "\",\"abbre_title\":\""+ lineArray[2] + "\",\"issn\":\""+ lineArray[3] + "\",\"cas_category\":\"" + lineArray[4] +"\",\"cas_index\":\""+lineArray[5]+"\",\"ifactor\":\""+lineArray[6] + "\"}\r\n");			
				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + num +1;
		}
		return "success convert";
		
	}
	
	/**
	 * 创建账户
	 * @param args
	 */
	@RequestMapping(value={"/create/account"}, method=RequestMethod.POST)
	@ResponseBody
	public String createAccount(@RequestBody List<Account> accountList) {
		String result = "";
		//若无pmid_title index 先创建
		if(!esUtil.indexExists("account")) {
			esUtil.createIndex("account");
		}
		for(Account account : accountList) {
			account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
			account.setPasswordConfirm(bCryptPasswordEncoder.encode(account.getPasswordConfirm()));
			
			SearchQuery searchQuery = new NativeSearchQueryBuilder()
					.withQuery(QueryBuilders.termQuery("userName", account.getUserName()))
					.withIndices("account")
					.withTypes("account_list")
					.build();
			
			if(esUtil.queryForIds(searchQuery).size() > 0) {
				result += account.getUserName() + "has existed ! " + "\r\n";
				continue;
			}
			
			IndexQuery indexQuery = new IndexQueryBuilder()
					.withId(account.getUserName())
					.withIndexName("account")
					.withObject(account)
					.withType("account_list")
					.build();
			String id = esUtil.index(indexQuery);
			if(id != null) {
				result +=  "create Account " + id + " successfully" + "\r\n";
			} else {
				result +=  "create Account " + id + " fail" + "\r\n";
			}
			
		}
		
		return result;				
	}
	
	/**
	 * crawle title abstract
	 * @return
	 */
	@RequestMapping(value={"/crawler/pmid2title/var"}, method=RequestMethod.GET)
	@ResponseBody
	public String crawlerTitleFromWeb() {
		
		//若无pmid_title index 先创建
		if(!esUtil.indexExists("pmid_title")) {
			esUtil.createIndex("pmid_title");
		}
		
		//查询pmid2title中已有的文章记录
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withIndices("pmid_title")
				.withTypes("pmid_title_list")
				.withFields("pmid")
				.build();
		
		List<String> pmidList = esUtil.queryAllKey(searchQuery, "pmid");
		
		//查询var2pmid 将其中不在pmidList的pmid 用爬虫进行爬去，并保存到es
		
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
				.mustNot(QueryBuilders.termsQuery("pmid", pmidList));
		
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(booleanQueryBuilder)
				.withTypes("var_pmid_list")
				.withIndices("var_pmid")
				.withFields("pmid")
				.build();
		
		List<String> wait2Crawler = esUtil.queryAllKey(searchQuery, "pmid");
		
		//进行网络爬虫爬取 并保存es
		if(wait2Crawler.size() > 0) {
			
			if(!esUtil.indexExists("pmid_title")) {
				esUtil.createIndex("pmid_title");
			}
			
			//交给另一个线程去处理，直接返回用户提示信息
			Executor executor = Executors.newCachedThreadPool();
			executor.execute(new CrawlerAndSaveTitleUtil(new ArrayList<>(wait2Crawler), esUtil));
			
		}
		
		return "backend is handing waiting please";
	}
	
	/**
	 * crawle title abstract
	 * @return
	 */
	@RequestMapping(value={"/crawler/pmid2title/gene"}, method=RequestMethod.GET)
	@ResponseBody
	public String crawlerTitleFromWebByGene() {
		
		//若无pmid_title index 先创建
		if(!esUtil.indexExists("pmid_title")) {
			esUtil.createIndex("pmid_title");
		}
		
		//查询pmid2title中已有的文章记录
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withIndices("pmid_title")
				.withTypes("pmid_title_list")
				.withFields("pmid")
				.build();
		
		List<String> pmidList =esUtil.queryAllKey(searchQuery, "pmid");
				
		//查询gene2pmid 将其中不在pmidList的pmid 用爬虫进行爬去，并保存到es
		
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
				.mustNot(QueryBuilders.termsQuery("pmid", pmidList));
		
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(booleanQueryBuilder)
				.withTypes("gene_pmid_list")
				.withIndices("gene_pmid")
				.withFields("pmid")
				.build();
		
		List<String> wait2Crawler = esUtil.queryAllKey(searchQuery, "pmid");
				
		//进行网络爬虫爬取 并保存es
		if(wait2Crawler.size() > 0) {
			
			if(!esUtil.indexExists("pmid_title")) {
				esUtil.createIndex("pmid_title");
			}
			
			//交给另一个线程去处理，直接返回用户提示信息
			Executor executor = Executors.newCachedThreadPool();
			executor.execute(new CrawlerAndSaveTitleUtil(new ArrayList<>(wait2Crawler), esUtil));
			
		}
		
		return "backend is handing waiting please";
	}
	
	
	/**
	 * crawle title abstract
	 * @return
	 */
	@RequestMapping(value={"/crawler/pmid2title/disease"}, method=RequestMethod.GET)
	@ResponseBody
	public String crawlerTitleFromWebByDisease() {
		
		//若无pmid_title index 先创建
		if(!esUtil.indexExists("pmid_title")) {
			esUtil.createIndex("pmid_title");
		}
		
		//查询pmid2title中已有的文章记录
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withIndices("pmid_title")
				.withTypes("pmid_title_list")
				.withFields("pmid")
				.build();
		
		List<String> pmidList = esUtil.queryAllKey(searchQuery, "pmid");
				
		//查询gene2pmid 将其中不在pmidList的pmid 用爬虫进行爬去，并保存到es
		
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
				.mustNot(QueryBuilders.termsQuery("pmid", pmidList));
		
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(booleanQueryBuilder)
				.withTypes("disease_pmid_list")
				.withIndices("disease_pmid")
				.withFields("pmid")
				.build();
		
		List<String> wait2Crawler = esUtil.queryAllKey(searchQuery, "pmid");
				
		//进行网络爬虫爬取 并保存es
		if(wait2Crawler.size() > 0) {
			
			if(!esUtil.indexExists("pmid_title")) {
				esUtil.createIndex("pmid_title");
			}
			
			//交给另一个线程去处理，直接返回用户提示信息
			Executor executor = Executors.newCachedThreadPool();
			executor.execute(new CrawlerAndSaveTitleUtil(new ArrayList<>(wait2Crawler), esUtil));
			
		}
		
		return "backend is handing waiting please";
	}
	
	/**
	 * crawle title abstract
	 * @return
	 */
	@RequestMapping(value={"/crawler/pmid2title/chemical"}, method=RequestMethod.GET)
	@ResponseBody
	public String crawlerTitleFromWebByChemical() {
		
		//若无pmid_title index 先创建
		if(!esUtil.indexExists("pmid_title")) {
			esUtil.createIndex("pmid_title");
		}
		
		//查询pmid2title中已有的文章记录
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withIndices("pmid_title")
				.withTypes("pmid_title_list")
				.withFields("pmid")
				.build();
		List<String> pmidList = new ArrayList<>();
		try {
			pmidList = esUtil.queryAllKey(searchQuery, "pmid");

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("queryAllPmid from pmid_title occur error owing to " + e.getMessage());
		}
						
		//查询gene2pmid 将其中不在pmidList的pmid 用爬虫进行爬去，并保存到es
		
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
				.mustNot(QueryBuilders.termsQuery("pmid", pmidList));
		
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(booleanQueryBuilder)
				.withTypes("chemical_pmid_list")
				.withIndices("chemcial_pmid")
				.withFields("pmid")
				.build();
		
		List<String> wait2Crawler = new ArrayList<>();
		try {
			wait2Crawler = esUtil.queryAllKey(searchQuery2, "pmid");

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("queryAllPmid from chemical_pmid occur error owing to " + e.getMessage());
		}
				
		//进行网络爬虫爬取 并保存es
		if(wait2Crawler.size() > 0) {
			
			if(!esUtil.indexExists("pmid_title")) {
				esUtil.createIndex("pmid_title");
			}
			
			//交给另一个线程去处理，直接返回用户提示信息
			Executor executor = Executors.newCachedThreadPool();
			executor.execute(new CrawlerAndSaveTitleUtil(new ArrayList<>(wait2Crawler), esUtil));
			
		}
		
		return "backend is handing waiting please";
	}
	
	@RequestMapping(value={"/crawler/pmid2titleDetail"})
	@ResponseBody
	public String crawlerTitleDetail() {
		//若无pmid_title index 先创建
		if(!esUtil.indexExists("pmid_title_detail")) {
			esUtil.createIndex("pmid_title_detail");
		}
		
		//查询pmid2title中已有的文章记录
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withIndices("pmid_title_detail")
				.withTypes("pmid_title_detail_list")
				.withFields("pmcid")
				.build();
		
		List<Map> pmcidList = esUtil.queryAll(searchQuery, Map.class);
		
		List<String> pmcidList2 = new ArrayList<>();
		for(Map single : pmcidList) {
			pmcidList2.add(single.get("pmcid").toString());
		}
		
		//查询pmid2Title 将其中不在pmcidList的pmcid 用爬虫进行爬去，并保存到es
		
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.existsQuery("pmcid"))
				.mustNot(QueryBuilders.termQuery("pmcid", ""))
				.mustNot(QueryBuilders.termsQuery("pmcid", pmcidList2));
		
		SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
				.withQuery(booleanQueryBuilder)
				.withTypes("pmid_title_list")
				.withIndices("pmid_title")
				.withFields("pmcid")
				.build();
		
		List<String> wait2Crawler = esUtil.queryAllKey(searchQuery2, "pmcid");
		
		//进行网络爬虫爬取 并保存es
		if(wait2Crawler.size() > 0) {
			
			if(!esUtil.indexExists("pmid_title_detail")) {
				esUtil.createIndex("pmid_title_detail");
			}
			
			//交给另一个线程去处理，直接返回用户提示信息
			Executor executor = Executors.newCachedThreadPool();
			
			int length = wait2Crawler.size()/ 100;
			
			int startPoint = 0;
			int endPoint = 0;
			for(int i =0; i<10; i++) {
				startPoint = length * i;
				endPoint = length * (i+1);
				executor.execute(new CrawlerAndSaveTitleDetail(new ArrayList<>(wait2Crawler.subList(startPoint, endPoint)), esUtil));

			}
			if(wait2Crawler.size() > (length * 10)-1) {
				executor.execute(new CrawlerAndSaveTitleDetail(new ArrayList<>(wait2Crawler.subList((length * 10)-1, wait2Crawler.size())), esUtil));
			}
			
		}
		
		return "backend is handing waiting please";
	}
	
	@RequestMapping(value={"/import/chemical/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String importChemical(@RequestParam("path") String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf("/")) + "/chemical.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split("\\t",5);
				
				result.append("{\"index\":{\"_index\":\"chemcial_pmid\",\"_type\":\"chemical_pmid_list\",\"_id\":" + num + "}}\r\n");
				if(lineArray.length == 4) {
					result.append("{\"pmid\":\"" + lineArray[0] + "\",\"mesh_id\":\""+ lineArray[1] + "\",\"mentions\":\""+ lineArray[2] + "\",\"resource\":\"" + lineArray[3] +"\",\"mesh_name\":\"\"}\r\n");			
				} else {
					result.append("{\"pmid\":\"" + lineArray[0] + "\",\"mesh_id\":\""+ lineArray[1] + "\",\"mentions\":\""+ lineArray[2] + "\",\"resource\":\"" + lineArray[3] +"\",\"mesh_name\":\""+lineArray[4] + "\"}\r\n");			
				}
				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + (num +1) + ", reason is " + e.getMessage();
		}
		return "success convert";
		
	}
	
	@RequestMapping(value={"/import/gene/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String importGene(@RequestParam("path") String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf("/")) + "/gene.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split("\\t",6);
				
				result.append("{\"index\":{\"_index\":\"gene_pmid\",\"_type\":\"gene_pmid_list\",\"_id\":" + num + "}}\r\n");

				if(lineArray.length == 4) {
					
					result.append("{\"pmid\":\"" + lineArray[0] + "\",\"gene_id\":\""+ lineArray[1] + "\",\"mentions\":\""+ lineArray[2] + "\",\"resource\":\"" + lineArray[3] +"\",\"symbol\":\"\",\"alias_symbol\":\"\"}\r\n");			

				} else {
					result.append("{\"pmid\":\"" + lineArray[0] + "\",\"gene_id\":\""+ lineArray[1] + "\",\"mentions\":\""+ lineArray[2] + "\",\"resource\":\"" + lineArray[3] +"\",\"symbol\":\""+lineArray[4] + "\",\"alias_symbol\":\"" +  lineArray[5] + "\"}\r\n");			
				
				}
				
				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + (num +1) + ", reason is " + e.getMessage();
		}
		return "success convert";
		
	}
	
	@RequestMapping(value={"/import/disease/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String importDisease(@RequestParam("path") String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf("/")) + "/disease.json");
		
		int num = 40529372;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split("\\t",5);
				
				result.append("{\"index\":{\"_index\":\"disease_pmid\",\"_type\":\"disease_pmid_list\",\"_id\":" + num + "}}\r\n");
				
				if(lineArray.length == 4) {
					result.append("{\"pmid\":\"" + lineArray[0] + "\",\"mesh_id\":\""+ lineArray[2] + "\",\"mentions\":\""+ lineArray[3] + "\",\"resource\":\"" + lineArray[1] +"\"\"}\r\n");			
				} else {
					result.append("{\"pmid\":\"" + lineArray[0] + "\",\"mesh_id\":\""+ lineArray[2] + "\",\"mentions\":\""+ lineArray[3] + "\",\"resource\":\"" + lineArray[1] +"\",\"mesh_name\":\""+lineArray[4] + "\"}\r\n");			

				}
				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(e.toString());
			return "fail convert owing to line " + (num +1) + ", reason is " + e.getMessage();
		}
		return "success convert";
		
	}
	
	@RequestMapping(value={"/import/chemical2mesh/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String importChemical2MeshID(@RequestParam("path") String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf("/")) + "/chemical2mesh.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split("\\t",3);
				
				result.append("{\"index\":{\"_index\":\"chemical_mesh\",\"_type\":\"chemical_mesh_list\",\"_id\":" + num + "}}\r\n");
				
				result.append("{\"source\":\"" + lineArray[2] + "\",\"chemical\":\""+ lineArray[1] + "\",\"meshId\":\""+ lineArray[0] + "\"}\r\n");			

				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(e.toString());
			return "fail convert owing to line " + (num +1) + ", reason is " + e.getMessage();
		}
		return "success convert";
		
	}
	
	
	@RequestMapping(value={"/import/disease2mesh/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String importDisease2MeshID(@RequestParam("path") String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf("/")) + "/disease2mesh.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split("\\t",3);
				
				result.append("{\"index\":{\"_index\":\"disease_mesh\",\"_type\":\"disease_mesh_list\",\"_id\":" + num + "}}\r\n");
				
				result.append("{\"source\":\"" + lineArray[1] + "\",\"disease\":\""+ lineArray[2] + "\",\"meshId\":\""+ lineArray[0] + "\"}\r\n");			

				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(e.toString());
			return "fail convert owing to line " + (num +1) + ", reason is " + e.getMessage();
		}
		return "success convert";
		
	}
	
	@RequestMapping(value={"/getAllPmids/es"}, method=RequestMethod.GET)
	@ResponseBody
	public String getAllPmids() {
				
		//output file
		File file2 = new File("/bioinfo/importfile/pmids.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
						
			String total = "";
			StringBuffer result = new StringBuffer();
			
			SearchQuery searchQuery = new NativeSearchQueryBuilder()
					.withQuery(QueryBuilders.matchAllQuery())
					.withIndices("pmid_title")
					.withTypes("pmid_title_list")
					.withFields("pmid")
					.build();
			
			List<String> pmids = esUtil.queryAllKey(searchQuery, "pmid");
			
			for(String pmid : pmids) {
				num++;
				result.append(pmid + "\r\n");
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
				
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + num +1;
		}
		return "success convert";
		
	}
	
	/**
	 * 添加pubDate 及 if 字段
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"/bioinfo/addField"}, method=RequestMethod.GET)
	public String addField() {
		
		try {
			
			Executor executor = Executors.newCachedThreadPool();
			
			SearchQuery searchQuery = new NativeSearchQueryBuilder()
					.withQuery(QueryBuilders.matchAllQuery())
					.withIndices("pmid_title")
					.withTypes("pmid_title_list")
					.withFields("pmid","pubDate", "issn","IF")
					.build();
			
			String scrollId = scan(searchQuery, 5000l, false);
			
			boolean hasRecords = true;
			while (hasRecords) {
				SearchResponse searchResponse = getClient().prepareSearchScroll(scrollId).
						setScroll(new TimeValue(5000l)).execute().actionGet();
				
				Page<Map> page = getResultsMapper().mapResults(searchResponse, Map.class, null);
				if(page.hasContent()) {
					
					List<Map> list= page.getContent();
					
					for(Map single : list) { //每一篇文章概要
						Map<String, String> map = new HashMap<>();
						IndexRequest indexRequest = new IndexRequest();
						if(single.get("pubDate") != null) {
							map.put("pubDate", single.get("pubDate").toString());
						}
						if(single.get("IF") != null) {
							map.put("IF", single.get("IF").toString());
						}
						if(single.get("issn") != null) {
							map.put("issn", single.get("issn").toString());
						}
						indexRequest.source(map);
						
						
						CountDownLatch countDownLatch = new CountDownLatch(4);
						//分别更新四大模块
						executor.execute(new UpdateModelByPmid(single.get("pmid").toString(), indexRequest, "var_pmid", "var_pmid_list", esUtil, countDownLatch));
						
						executor.execute(new UpdateModelByPmid(single.get("pmid").toString(), indexRequest, "gene_pmid", "gene_pmid_list", esUtil, countDownLatch));
						
						executor.execute(new UpdateModelByPmid(single.get("pmid").toString(), indexRequest, "disease_pmid", "disease_pmid_list", esUtil, countDownLatch));
						
						executor.execute(new UpdateModelByPmid(single.get("pmid").toString(), indexRequest, "chemcial_pmid", "chemical_pmid_list", esUtil, countDownLatch));
						
						countDownLatch.await();
					}
					
					scrollId = searchResponse.getScrollId();

				} else {
					hasRecords = false;
				}
					
			}
			clearScroll(scrollId);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("addField errer owing to " + e.getMessage());
			return "fail";
		}
		
		return "successful";
	}
	
	
	@RequestMapping(value={"/convert/pmid2title"}, method=RequestMethod.GET)
	@ResponseBody
	public String ConvertTitleAbstract2ESFormat(@RequestParam String path) {
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
		
		//output file
		File file2 = new File(path.substring(0, path.lastIndexOf(".txt")) + "_pmidtitle.json");
		
		int num = 0;
		
		try {
			
			FileOutputStream fo = new FileOutputStream(file2, true);
			OutputStreamWriter bw = new OutputStreamWriter(fo, "UTF-8");
			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
			
			String line = buffer.readLine();
			
			String total = "";
			StringBuffer result = new StringBuffer();
			while(null != (line = buffer.readLine())) {
				String[] lineArray = line.split("\\t",12);
				
				result.append("{\"index\":{\"_index\":\"pmid_title\",\"_type\":\"pmid_title_list\",\"_id\":" + lineArray[0] + "}}\r\n");
				
				result.append("{\"pmid\":\"" + lineArray[0] + "\",\"doi\":\""+ lineArray[1] + "\",\"pubDate\":\""+ convertDate(lineArray[2]) + "\",\"title\":\"" + lineArray[3] +"\",\"context\":\""+lineArray[4] +  "\",\"pmcid\":\"" + lineArray[5] + "\",\"journal\":\"" + lineArray[6] + "\",\"authorAddress\":\"" + lineArray[7] + "\",\"issn\":\"" + lineArray[8] + "\",\"casCategory\":\"" + lineArray[9] + "\",\"casIndex\":\"" + lineArray[10] +  "\",\"IF\":\"" + lineArray[11] + "\",\"highLight\":\"\",\"lightContext\":\"\"" +  "}\r\n");			

				num++;
				
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + (num +1) + ", reason is " + e.getMessage();
		}
		return "success convert";
	}
	
	private String convertDate(String date) {
		String [] dataArray = date.split("\\s");
		
		String month = "01";
		if(dataArray.length >1) {
			switch (dataArray[1]) {
			case "Jan":
				month = "01";
				break;

			case "Feb":
				month = "02";
				break;
			
			case "Mar":
				month = "03";
				break;
				
			case "Apr":
				month = "04";
				break;
			case "May":
				month = "05";
				break;
			case "Jun":
				month = "06";
				break;
			case "Jul":
				month = "07";
				break;
			case "Aug":
				month = "08";
				break;
			case "Sep":
				month = "09";
				break;
			case "Oct":
				month = "10";
				break;
			case "Nov":
				month = "11";
				break;
			case "Dec":
				month = "12";
				break;
			
			}
		} else  {
			month = "01";
		}
		
		return dataArray[0] + "-" + month + "-01";
		
	}
	
	@ResponseBody
	@RequestMapping(value={"/get/all/pmcids"}, method=RequestMethod.GET)
	public String getAllPmcids() {
		//output file
		File file2 = new File("/mnt/bioinfo/importfile/pmicds.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
						
			String total = "";
			StringBuffer result = new StringBuffer();
			
			BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
					.must(QueryBuilders.existsQuery("pmcid"))
					.mustNot(QueryBuilders.termQuery("pmcid", ""));
			
			SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
					.withQuery(booleanQueryBuilder)
					.withTypes("pmid_title_list")
					.withIndices("pmid_title")
					.withFields("pmcid")
					.build();
			
			List<String> wait2Crawler = esUtil.queryAllKey(searchQuery2, "pmcid");
			
			for(String pmid : wait2Crawler) {
				num++;
				if(pmid == null || pmid =="") {
					continue;
				}
				result.append(pmid + "\r\n");
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
				
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + num +1;
		}
		return "success convert";
		
	}
	
	@ResponseBody
	@RequestMapping(value={"/get/notrepeat/pmcids"}, method=RequestMethod.GET)
	public String getAllNotRepeatPmcids() {
		//output file
		File file2 = new File("/mnt/bioinfo/importfile/pmicds.json");
		
		int num = 0;
		
		try {
			
			FileWriter fw = new FileWriter(file2, true);
			BufferedWriter bw = new BufferedWriter(fw);
						
			String total = "";
			StringBuffer result = new StringBuffer();
			
			BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
					.must(QueryBuilders.existsQuery("pmcid"))
					.mustNot(QueryBuilders.termQuery("pmcid", ""));
			
			SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
					.withQuery(booleanQueryBuilder)
					.withTypes("pmid_title_list")
					.withIndices("pmid_title")
					.withFields("pmcid")
					.build();
			
			List<String> wait2Crawler = esUtil.queryAllKey(searchQuery2, "pmcid");
			Set<String> wait2CrawlerSet = new HashSet<>(wait2Crawler);
			
			SearchQuery searchQuery3 = new NativeSearchQueryBuilder()
					.withQuery(booleanQueryBuilder)
					.withTypes("pmid_title_detail_list")
					.withIndices("pmid_title_detail")
					.withFields("pmcid")
					.build();
			List<String> wait2Crawler2 = esUtil.queryAllKey(searchQuery3, "pmcid");
			Set<String> hasCrawleredSet = new HashSet<>(wait2Crawler2);
			
			Set<String> hasCrawleredSet2 = new HashSet<>();
			hasCrawleredSet.forEach(string -> hasCrawleredSet2.add("PMC"+string));
			logger.info("has crawled pmcids is " + hasCrawleredSet2.size());
			logger.info("all pmcids is " +wait2CrawlerSet.size());
			wait2CrawlerSet.removeAll(hasCrawleredSet2);
			
			logger.info("true waiting to crawlered pmcid is " + wait2CrawlerSet);
			for(String pmid : wait2CrawlerSet) {
				num++;
				if(pmid == null || pmid =="") {
					continue;
				}
				result.append(pmid + "\r\n");
				if(num % 1000 == 0) {
					total = result.toString();
					bw.write(total);
					result = new StringBuffer();
				}
				
			}
			
			total = result.toString();
			bw.write(total);
			
			bw.flush();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to line " + num +1;
		}
		return "success convert";
		
	}
	
	@RequestMapping(value={"/crawler/pmid2titledetail"})
	@ResponseBody
	public String crawlerTitledetail(@RequestParam String path) {
		
		
		//交给另一个线程去处理，直接返回用户提示信息
		Executor executor = Executors.newCachedThreadPool();
		
		//input file
		File file = new File(path);
		BufferedReader buffer;
					
		try {			
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
									
			String line = "";
			long count = 0l;
			List<String> pmcids = new ArrayList<>();
			while(null != (line = buffer.readLine())) {
				
				pmcids.add(line.trim());
				count ++;
				if(count % 10000 == 0) {
					logger.info("100000 pmids has added into list");
					executor.execute(new CrawlerAndSaveTitleDetail(pmcids, esUtil));
					pmcids = new ArrayList<>();
				}
				
			}
			
			buffer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail convert owing to " + e.getMessage();
		}
		return "success import";
			
	}
	
	@RequestMapping(value = {"/save/all/pmids/to/redis"}, method=RequestMethod.GET)
	@ResponseBody
	public String getAndSaveAllModelPmid(@RequestParam String name) {
		
		String index = "";
		String type = "";
		
		if("var".equals(name)) {
			index = "var_pmid";
			type = "var_pmid_list";
		}
		if("gene".equals(name)) {
			index = "gene_pmid";
			type = "gene_pmid_list";
		}
		if("disease".equals(name)) {
			index = "disease_pmid";
			type = "disease_pmid_list";
		}
		if("chemical".equals(name)) {
			index = "chemcial_pmid";
			type = "chemical_pmid_list";
		}
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withTypes(type)
				.withIndices(index)
				.withFields("pmid")
				.build();
		
		List<String> pmids = esUtil.queryAllKey(searchQuery, "pmid");
		Set<String> varPmids = new HashSet<>(pmids);
		logger.info(name + " pmids has " + varPmids.size());
		
		boolean var = redis.set(name + "_pmids", varPmids);
		
		if(var) {
			return "success save to redis";
		} else {
			return "fail to save to redis";
		}
	}
	
}
