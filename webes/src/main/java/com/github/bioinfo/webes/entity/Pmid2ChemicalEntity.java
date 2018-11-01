package com.github.bioinfo.webes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="chemcial_pmid", type="chemical_pmid_list")
public class Pmid2ChemicalEntity {

	@Id
	private int _id;
	private String pmid;
	private String mesh_id;
	private String mentions;
	private String resource;
	private String mesh_name;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getMesh_id() {
		return mesh_id;
	}
	public void setMesh_id(String mesh_id) {
		this.mesh_id = mesh_id;
	}
	public String getMentions() {
		return mentions;
	}
	public void setMentions(String mentions) {
		this.mentions = mentions;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getMesh_name() {
		return mesh_name;
	}
	public void setMesh_name(String mesh_name) {
		this.mesh_name = mesh_name;
	}
	
	
}
