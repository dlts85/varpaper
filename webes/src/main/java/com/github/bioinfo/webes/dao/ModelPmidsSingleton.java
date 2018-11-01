package com.github.bioinfo.webes.dao;

import java.util.HashSet;
import java.util.Set;

public class ModelPmidsSingleton {
	
	private static Set<String> varSets = new HashSet<>();
	private static Set<String> geneSets = new HashSet<>();
	private static Set<String> diseaseSets = new HashSet<>();
	private static Set<String> chemicalSets = new HashSet<>();
	
	public static void setModel(Set<String> vars, String name) {
		if("var".equals(name)) {
			varSets = vars;
		}
		if("gene".equals(name)) {
			geneSets = vars;
		}
		if("disease".equals(name)) {
			diseaseSets = vars;
		}
		if("chemical".equals(name)) {
			chemicalSets = vars;
		}
	}
	
	public static Set<String> getModelSet(String name) {
		if("var".equals(name)) {
			return varSets;
		}
		if("gene".equals(name)) {
			return geneSets;
		}
		if("disease".equals(name)) {
			return diseaseSets;
		}
		if("chemical".equals(name)) {
			return chemicalSets;
		}
		return null;
	}

}
