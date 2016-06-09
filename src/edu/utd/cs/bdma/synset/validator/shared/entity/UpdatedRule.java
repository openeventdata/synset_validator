package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UpdatedRule implements Serializable{

	@Id Long id;
	@Index Long idUser;
	@Index Long idTranslatedRule;
	
	String updatedText;
	Date submissionTime;
	
	
	public UpdatedRule() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getIdTranslatedRule() {
		return idTranslatedRule;
	}

	public void setIdTranslatedRule(Long idTranslatedRule) {
		this.idTranslatedRule = idTranslatedRule;
	}

	public Long getId() {
		return id;
	}

	public String getUpdatedText() {
		return updatedText;
	}
	
	public void setUpdatedText(String updatedText) {
		this.updatedText = updatedText;
	}
	
	public Date getSubmissionTime() {
		return submissionTime;
	}
	
	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}
	
	public UpdatedRule(String updatedText) {
		super();
		this.updatedText = updatedText;
		this.submissionTime = new Date(System.currentTimeMillis());
	}
	
	
	
}
