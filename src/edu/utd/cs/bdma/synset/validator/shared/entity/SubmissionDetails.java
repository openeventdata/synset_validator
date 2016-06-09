package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

public class SubmissionDetails implements Serializable{
	
	public SubmissionDetails() {
		// TODO Auto-generated constructor stub
	}
	
	
	Long submissionId;
	String word;
	String cameoCode;
	String cameoDef;
	String cameoDescription;
	
	public Long getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getCameoCode() {
		return cameoCode;
	}
	public void setCameoCode(String cameoCode) {
		this.cameoCode = cameoCode;
	}
	public String getCameoDef() {
		return cameoDef;
	}
	public void setCameoDef(String cameoDef) {
		this.cameoDef = cameoDef;
	}
	public String getCameoDescription() {
		return cameoDescription;
	}
	public void setCameoDescription(String cameoDescription) {
		this.cameoDescription = cameoDescription;
	}
	
	public boolean contains(String s){
		return word.contains(s) || cameoCode.contains(s) || cameoDescription.contains(s);
	}

}
