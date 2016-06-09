package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class CameoTranslatedRule implements Serializable{
    @Id Long id;
    @Index Long ruleID;
    @Index String languageCode;
	String text;
	String source;
	boolean verified;
	
	public CameoTranslatedRule(String languageCode, String text, String source, boolean verified) {
		super();
		this.languageCode = languageCode;
		this.text = text;
		this.source = source;
		this.verified = verified;
	}
	
	public Long getId() {
		return id;
	}
	
	public CameoTranslatedRule() {
		// TODO Auto-generated constructor stub
	}
	
	public void setRuleID(Long ruleID) {
		this.ruleID = ruleID;
	}
	
	public Long getRuleID() {
		return ruleID;
	}
	
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	
	
	
}
