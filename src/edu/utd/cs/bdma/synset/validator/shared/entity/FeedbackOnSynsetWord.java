package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class FeedbackOnSynsetWord implements Serializable{

	@Ignore public static final String CORRECT = "c";
	@Ignore public static final String INCORRECT = "ic";
	@Ignore public static final String AMBIGUOUS = "a";
	
	@Id Long id;
	
	@Index 
	Long idWord;
	
    @Index Long submissionId;
	
	String country;
	
	String verdict;
	String comment;
	
	public FeedbackOnSynsetWord() {
		// TODO Auto-generated constructor stub
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountry() {
		return country;
	}
	
	public Long getSubmissionId() {
		return submissionId;
	}
	
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	
	public void setIdWord(Long idWord) {
		this.idWord = idWord;
	}
	
	public Long getId() {
		return id;
	}
	

	public Long getIdWord() {
		return idWord;
	}
	
	public String getVerdict() {
		return verdict;
	}
	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public FeedbackOnSynsetWord(String verdict, String comment) {
		super();
		this.verdict = verdict;
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+submissionId+" "+verdict+" "+comment;
	}
	
	
	
}
