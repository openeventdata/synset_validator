package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.sql.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class VerdictOnRule implements Serializable{
	@Ignore public static final String CORRECT = "c";
	@Ignore public static final String INCORRECT = "ic";
	@Ignore public static final String AMBIGUOUS = "a";
	
	@Id Long id;
	@Index Long idTranslatedRule;
	@Index Long submissionId;
	String verdict;
	String comment;
	Date submissionTime;
	
	
	public Long getId() {
		return id;
	}
	
	public VerdictOnRule() {
		// TODO Auto-generated constructor stub
	}

	public Long getIdTranslatedRule() {
		return idTranslatedRule;
	}

	public void setIdTranslatedRule(Long idTranslatedRule) {
		this.idTranslatedRule = idTranslatedRule;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	
	public Long getSubmissionId() {
		return submissionId;
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

	public Date getSubmissionTime() {
		return submissionTime;
	}

	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}

	
	
	public VerdictOnRule(String verdict, String comment) {
		// TODO Auto-generated constructor stub
		this.verdict = verdict;
		this.comment = comment;
		this.submissionTime = new Date(System.currentTimeMillis());
	}
	

}
