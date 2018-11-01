package com.github.bioinfo.webes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;

public class UpdateModelByPmid implements Runnable {

	private String pmid;
	private String indexName;
	private String indexType;
	private IndexRequest request;
	private EsScrollUtil util;
	private CountDownLatch countDownLatch;
	
	public UpdateModelByPmid(String pmid, IndexRequest request, String indexName, String indexType, EsScrollUtil esScrollUtil, CountDownLatch countDownLatch) {
		this.indexName = indexName;
		this.pmid = pmid;
		this.indexType = indexType;
		this.request = request;
		this.util = esScrollUtil;
		this.countDownLatch = countDownLatch;
	}
	
	@Override
	public void run() {
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.termQuery("pmid", pmid))
				.withIndices(indexName)
				.withTypes(indexType)
				.build();
		
		List<String> ids = util.queryForIds(searchQuery);
		
		int i = 0;
		List<UpdateQuery> queries = new ArrayList<>();
		if(!ids.isEmpty()) {
			for(String id : ids) {
				i++;
				UpdateQuery updateQuery = new UpdateQueryBuilder()
						.withId(id)
						.withIndexName(indexName)
						.withType(indexType)
						.withIndexRequest(request)
						.build();
				queries.add(updateQuery);
				if(i % 10000 == 0) {
					util.bulkUpdate(queries);
				}
			}
			util.bulkUpdate(queries);
		}
		
		countDownLatch.countDown();
		
	}

}
