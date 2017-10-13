package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class SynsetExampleVerdict implements Serializable{

	@Id Long id;
	@Index Long synsetExampleId;
	@Index Long submissionId;
	int verdict;
	
	public Long getId() {
		return id;
	}
	
	public Long getSubmissionId() {
		return submissionId;
	}
	
	public Long getSynsetExampleId() {
		return synsetExampleId;
	}
	
	public int getVerdict() {
		return verdict;
	}
	
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	
	public void setSynsetExampleId(Long synsetExampleId) {
		this.synsetExampleId = synsetExampleId;
	}
	
	public void setVerdict(int verdict) {
		this.verdict = verdict;
	}
	
	public SynsetExampleVerdict() {
		// TODO Auto-generated constructor stub
	}
	
	
}
