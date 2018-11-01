package com.github.bioinfo.webes.model;

import java.util.ArrayList;
import java.util.List;

public class TotalNumAndEntities<T> {

	private long num;
	private List<T> entites = new ArrayList<>();
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public List<T> getEntites() {
		return entites;
	}
	public void setEntites(List<T> entites) {
		this.entites = entites;
	}
	
	public boolean isEmpty() {
		return entites.isEmpty();
	}
}
