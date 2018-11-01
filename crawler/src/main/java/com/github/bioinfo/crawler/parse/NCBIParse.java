package com.github.bioinfo.crawler.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.github.bioinfo.crawler.entity.Pmid2TitleDetailEntity;
import com.github.bioinfo.crawler.entity.Pmid2TitleEntity;
import com.github.bioinfo.crawler.entity.TitleSectionEntity;
import com.github.bioinfo.crawler.util.CommonUtil;

/**
 * 解析xml文件
 * @author fujian
 *
 */
public class NCBIParse {

	public static List<Pmid2TitleEntity> getTitleInfo(String xml) {
		
		List<Pmid2TitleEntity> data = new ArrayList<Pmid2TitleEntity>();
		
		Document doc = Jsoup.parse(xml);
		Elements elements = doc.select("PubmedArticle");
		
		for(Element element : elements) {
			
			String pubDateStr ="";
			Date pubDate = null;
			String title ="";
			String pmid ="";
			String pmcid = "";
			String doi ="";
			String author = "";
			String journal ="";
			String ifactor ="";
			String casCategory ="";
			String casIndex ="";
			String context ="";
			String issn = "";
			
			Pmid2TitleEntity entity = new Pmid2TitleEntity();
			
			Element pmidEle = element.selectFirst("MedlineCitation PMID");
			if(null != pmidEle) {
				pmid = pmidEle.text();
			}
			
			Element pubDateEle = element.selectFirst("MedlineCitation DateCompleted");
			if(null != pubDateEle) {
				if(null != pubDateEle.select("Year")) {
					pubDateStr = pubDateEle.select("Year").text();
				}
				if(null != pubDateEle.select("Month")) {
					pubDateStr += "-" + pubDateEle.select("Month").text();
				}
				if(null != pubDateEle.select("Day")) {
					pubDateStr += "-" + pubDateEle.select("Day").text();
				}
			}
			
			pubDate = CommonUtil.convertStrDate2Date(pubDateStr, null);
			
			
			Element issEle = element.selectFirst("MedlineCitation MedlineJournalInfo ISSNLinking");
			if(null != issEle) {
				issn = issEle.text().trim();
			}
			
			Element journalEle = element.selectFirst("MedlineCitation Article Journal Title");
			if(null != journalEle) {
				journal = journalEle.text();
			}
			
			Elements titleEle = element.select("MedlineCitation Article ArticleTitle");
			if(null != titleEle) {
				title = titleEle.text();
			}
			
			Elements abstractEles = element.select("MedlineCitation Article Abstract AbstractText");
			if(null != abstractEles) {
				for(Element single : abstractEles) {
					context += single.text() + " ";
				}
			}
			
			Element authorsEle = element.selectFirst("MedlineCitation Article AuthorList");
			if(null != authorsEle) {
				Elements authors = authorsEle.select("Author");
				int i=0;
				for(Element authorEle : authors) {
					if(3 == i) {
						break;
					}
					author += authorEle.select("LastName").text() + " " + authorEle.select("Initials").text() + ", ";
					i++;
				}
			}
			
			Element doiEle = element.selectFirst("PubmedData ArticleIdList ArticleId[IdType=doi]");
			if(null != doiEle) {
				doi = doiEle.text();
			}
			
			Element pmcEle = element.selectFirst("PubmedData ArticleIdList ArticleId[IdType=pmc]");
			if(null != pmcEle) {
				pmcid = pmcEle.text();
			}
			
			entity.setAuthorAddress(author);
			entity.setContext(context);
			entity.setDoi(doi);
			entity.setJournal(journal);
			entity.setPmid(pmid);
			entity.setTitle(title);
			entity.setPubDate(pubDate);
			entity.setIssn(issn);
			entity.setPmcid(pmcid);
			data.add(entity);
			
		}
		return data;
		
	}
	
	/**
	 * 解析文章正文
	 * @param xml
	 * @return
	 */
	public static List<Pmid2TitleDetailEntity> getTitleDetail(String xml) {
		
		List<Pmid2TitleDetailEntity> data = new ArrayList<Pmid2TitleDetailEntity>();
		
		Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
		Elements elements = doc.select("article");
		
		for(Element element : elements) {
			
			String title ="";
			String pmid ="";
			String pmcid = "";			
			Pmid2TitleDetailEntity entity = new Pmid2TitleDetailEntity();
			
			Element pmidEle = element.selectFirst("front article-meta article-id[pub-id-type=pmid]");
			if(null != pmidEle) {
				pmid = pmidEle.text();
			}
			
			Element pmcidEle = element.selectFirst("front article-meta article-id[pub-id-type=pmc]");
			if(null != pmcidEle) {
				pmcid = pmcidEle.text();
			}
			
			Element titleEle = element.selectFirst("front article-meta title-group article-title");
			if(null != titleEle) {
				title = titleEle.text();
			}
			
			Elements sections = element.select("body > sec");
			if(null != sections) {
				
				List<TitleSectionEntity> content = new ArrayList<TitleSectionEntity>();
				
				for(Element sec : sections) {
					
					List<String> paras = new ArrayList<String>();
					String title_ = "";
					TitleSectionEntity sectionEntity = new TitleSectionEntity();

					
					title_ = sec.selectFirst("title").text();
					
					Elements pElements = sec.select("p");
					pElements.select("italic").unwrap();	
					pElements.select("xref").unwrap();
					pElements.select("sub").unwrap();
					
					if(null != pElements) {
						for(Element p : pElements) {
							paras.add(p.text());
						}
					}
					
					sectionEntity.setParas(paras);
					sectionEntity.setTitle(title_);
					content.add(sectionEntity);
				}
				
				entity.setContent(content);
			}
			
			entity.setPmcid(pmcid);
			entity.setTitle(title);
			entity.setPmid(pmid);
			data.add(entity);
			
		}
		return data;	
	}
}
