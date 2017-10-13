package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

public class CameoEntrySummery implements Serializable{
	
	private String code;
	private String concept;
	private String description;
	
	public CameoEntrySummery() {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean contains(String searchText){
		return (code +" "+concept + " "+description).contains(searchText);
	}
	
	

}
