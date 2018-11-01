package com.github.bioinfo.webes.entity;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="disease_mesh", type="disease_mesh_list")

public class Disease2MeshEntity {
	
	private String source;
	private String disease;
	private String meshId;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getMeshId() {
		return meshId;
	}
	public void setMeshId(String meshId) {
		this.meshId = meshId;
	}
	
	

}
