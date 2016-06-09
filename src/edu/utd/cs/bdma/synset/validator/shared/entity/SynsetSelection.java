package edu.utd.cs.bdma.synset.validator.shared.entity;


import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SynsetSelection implements Serializable{

	@Id Long id;
	
	
	@Index Long idSynset;
	@Index Long submissionId;
	
	Date submissionTime;

	
	public Long getSubmissionId() {
		return submissionId;
	}
	
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}


	public Long getIdSynset() {
		return idSynset;
	}

	public void setIdSynset(Long idSynset) {
		this.idSynset = idSynset;
	}

	public Long getId() {
		return id;
	}

	public Date getSubmissionTime() {
		return submissionTime;
	}

	public Date getDate() {
		return submissionTime;
	}

	public SynsetSelection(Long submissionId, Long idSynset) {
		super();
		this.idSynset = idSynset;
		this.submissionId = submissionId;
	}
	
	public SynsetSelection() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
