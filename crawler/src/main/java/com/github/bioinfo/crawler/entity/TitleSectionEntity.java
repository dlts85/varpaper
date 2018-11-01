package com.github.bioinfo.crawler.entity;

import java.util.List;

public class TitleSectionEntity {

	private String title;
	private List<String> paras;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getParas() {
		return paras;
	}
	public void setParas(List<String> paras) {
		this.paras = paras;
	}
}
