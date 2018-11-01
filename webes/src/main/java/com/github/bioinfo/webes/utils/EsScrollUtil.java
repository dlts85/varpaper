package com.github.bioinfo.webes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import com.github.bioinfo.webes.model.TotalNumAndEntities;

@Component
public class EsScrollUtil extends ElasticsearchTemplate {

	public EsScrollUtil(Client client) {
		super(client);
	}

/*	@Autowired
	private ElasticsearchTemplate template;*/
	
	/**
	 * query all by searchQuery from es
	 * @author fujian
	 *
	 */
	public <T> List<T> queryAll(SearchQuery searchQuery, Class<T> T) {
		
		String scrollId = scan(searchQuery, 5000l, false);
		
		List<T> entities = new ArrayList<>();
		boolean hasRecords = true;
		while (hasRecords) {
			SearchResponse searchResponse = getClient().prepareSearchScroll(scrollId).
					setScroll(new TimeValue(5000l)).execute().actionGet();
			
			Page<T> page = getResultsMapper().mapResults(searchResponse, T, null);
			if(page.hasContent()) {
				entities.addAll(page.getContent());
				scrollId = searchResponse.getScrollId();
			} else {
				hasRecords = false;
			}
		}
		
		clearScroll(scrollId);
		
		return entities;
		
	}
	
	/**
	 * 返回 key List<String>
	 * @param searchQuery
	 * @return
	 */
	public List<String> queryAllKey(SearchQuery searchQuery, String key) {
		
		String scrollId = scan(searchQuery, 60000l, false);
		
		List<String> entities = new ArrayList<>();
		boolean hasRecords = true;
		while (hasRecords) {
			SearchResponse searchResponse = getClient().prepareSearchScroll(scrollId).
					setScroll(new TimeValue(60000l)).execute().actionGet();
			
			Page<Map> page = getResultsMapper().mapResults(searchResponse, Map.class, null);
			if(page.hasContent()) {
				page.getContent().forEach(map -> entities.add(map.get(key).toString()));
				scrollId = searchResponse.getScrollId();
			} else {
				hasRecords = false;
			}
		}
		
		clearScroll(scrollId);
		
		return entities;
		
	}
	
	/**
	 * 返回 总记录数及前n条document List<T>
	 * @param searchQuery
	 * @param n
	 * @param T
	 * 
	 * @return
	 */
	public <T> TotalNumAndEntities<T> queryNAndTotalNum(SearchQuery searchQuery, Integer n, Class<T> T) {
		
		List<T> entities = new ArrayList<>();
		long totalNum = 0L;
		TotalNumAndEntities<T> result = new TotalNumAndEntities<>();
		
		String scrollId = scan(searchQuery, 5000l, false);
		//boolean isN = false;

		boolean hasRecords = true;
		while (hasRecords) {
			SearchResponse searchResponse = getClient().prepareSearchScroll(scrollId).
					setScroll(new TimeValue(5000l)).execute().actionGet();
			
			//totalNum += searchResponse.getHits().getTotalHits();

			/*if(isN) {
				if(!searchResponse.isContextEmpty()) {
					scrollId = searchResponse.getScrollId();
				} else {
					hasRecords = false;
				}
				
			}*/ 
			Page<T> page = getResultsMapper().mapResults(searchResponse, T, null);
			if(page.hasContent()) {
				entities.addAll(page.getContent());
				if(entities.size() == n) {
					//isN = true;
					hasRecords = false;
					totalNum = searchResponse.getHits().totalHits();
				}
				scrollId = searchResponse.getScrollId();
			} else {
				hasRecords = false;
				totalNum = searchResponse.getHits().totalHits();
			}
			
		}
		
		clearScroll(scrollId);
		
		result.setNum(totalNum);
		result.setEntites(entities);
		return result;
		
	}
	
	/**
	 * highLight
	 * @param index
	 * @param type
	 * @param field
	 * @param builder
	 * @return
	 */
	public List<String> getHightLight(String index, String type, String field, BoolQueryBuilder builder, int size) {
		
		List<String> hight2Return = new ArrayList<>();
		
		SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(index);
		searchRequestBuilder
			.setTypes(type)
			.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			.setQuery(builder)
			.setExplain(true)
			.addHighlightedField(field)
			//.setHighlighterType("fast-vector-highlighter")
			.setHighlighterPreTags("<span class=\"match\">")
			.setHighlighterPostTags("</span>")
			.setHighlighterFragmentSize(500)
			.setHighlighterNumOfFragments(size);
		
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		
		for(int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			Map<String, HighlightField> results = hit.highlightFields();
			HighlightField targetHighLight = results.get(field);
			if(null != targetHighLight) {
				Text[] texts = targetHighLight.fragments();
				for(int j = 0; j < texts.length; j++) {
					/*if(3 == j) {
						break;
					}*/
					String text = texts[j].toString();
					hight2Return.add(text);
				}
			}
		}
		return hight2Return;
	}
	
}
