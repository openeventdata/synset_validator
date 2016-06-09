package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class CameoSelectedSynset implements Serializable{
	
	public CameoSelectedSynset() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Id Long id;
	
	@Index Long submissionId;
	Date submissionTime;
	
	@Index Long idSynsetEntry;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	
	public Long getSubmissionId() {
		return submissionId;
	}
	
	
	public Long getIdSynsetEntry() {
		return idSynsetEntry;
	}
	public void setIdSynsetEntry(Long idSynsetEntry) {
		this.idSynsetEntry = idSynsetEntry;
	}
	
	public Date getSubmissionTime() {
		return submissionTime;
	}
	
	
	
	

}
