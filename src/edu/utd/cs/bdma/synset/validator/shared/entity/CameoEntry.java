package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class CameoEntry implements Serializable{

	@Id Long id;
	@Index String code;
	String concept;
	
	String name;
	String description;
	String usageNotes;
	ArrayList<String> examples;
	String exampleNotes;
	
	public CameoEntry(String concept, String code, String name, String description, String usageNotes) {
		super();
		this.code = code;
		this.concept = concept;
		this.name = name;
		this.description = description;
		this.usageNotes = usageNotes;
		examples = new ArrayList<String>();
		exampleNotes = "";
	}
	
	public CameoEntry() {
		// TODO Auto-generated constructor stub
	}
	
	public String getCode() {
		return code;
	}
	
	
	
	public String getConcept() {
		return concept;
	}
	
	public void setConcept(String concept) {
		this.concept = concept;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(String id) {
		this.code = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUsageNotes() {
		return usageNotes;
	}
	public void setUsageNotes(String usageNotes) {
		this.usageNotes = usageNotes;
	}
	public void addExample(String example){
		examples.add(example);
	}
	
	public ArrayList<String> getExamples() {
		return examples;
	}
	
	public void setExampleNotes(String exampleNotes) {
		this.exampleNotes = exampleNotes;
	}
	
	public String getExampleNotes() {
		return exampleNotes;
	}
	
	public String summery(){
		return code+": "+concept.toLowerCase()+","+name;
	}
	
	
	public CameoEntrySummery summerize(){
		CameoEntrySummery summery = new CameoEntrySummery();
		summery.setCode(code);
		summery.setConcept(concept);
		summery.setDescription(description);
		
		return summery;
	}
	
}
