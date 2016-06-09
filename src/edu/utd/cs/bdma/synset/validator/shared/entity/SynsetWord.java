package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;


@Entity
public class SynsetWord implements Serializable{
	
	@Id Long id;
	@Index Long idSynsetEntry;
	
	String word;
	@Index String languageCode;
	@Index Long submissionId;
	
	public SynsetWord() {
		// TODO Auto-generated constructor stub
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getSubmissionId() {
		return submissionId;
	}
	
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	
	public void setIdSynsetEntry(Long idSynsetEntry) {
		this.idSynsetEntry = idSynsetEntry;
	}
	
	public Long getIdSynsetEntry() {
		return idSynsetEntry;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getLanguageCode() {
		return languageCode;
	}
	
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
	public SynsetWord(String word, String languageCode) {
		super();
		this.word = word;
		this.languageCode = languageCode;
	}
	
	
	
	

}
