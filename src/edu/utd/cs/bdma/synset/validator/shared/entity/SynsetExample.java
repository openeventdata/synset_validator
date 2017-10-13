package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SynsetExample implements Serializable{

	@Id Long id;
	@Index
	Long synsetId;
	String example;
	String source;
	
	public Long getSynsetId() {
		return synsetId;
	}
	public void setSynsetId(Long synsetId) {
		this.synsetId = synsetId;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getId() {
		return id;
	}
	
	public SynsetExample() {
		// TODO Auto-generated constructor stub
	}
	
	
}
