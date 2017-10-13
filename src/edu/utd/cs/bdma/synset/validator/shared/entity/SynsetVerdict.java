package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SynsetVerdict implements Serializable{
	@Ignore public static final String CORRECT = "c";
	@Ignore public static final String INCORRECT = "ic";
	@Ignore public static final String AMBIGUOUS = "a";
	
	@Id Long id;
	@Index 
	Long submissionId;
	
	@Index
	Long synsetId;
	
	String verdict;

	public Long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}

	public Long getSynsetId() {
		return synsetId;
	}

	public void setSynsetId(Long synsetId) {
		this.synsetId = synsetId;
	}

	public String getVerdict() {
		return verdict;
	}

	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}

	public Long getId() {
		return id;
	}
	
	public SynsetVerdict() {
		// TODO Auto-generated constructor stub
	}
	
	
	

}
