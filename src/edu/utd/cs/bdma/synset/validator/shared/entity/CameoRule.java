package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class CameoRule implements Serializable{

	@Id Long id;
	String ruleText;
	@Index String cameoCode;
	@Index String word;
	@Index String langCode;
	
	@Index String source;
	
	
	public Long getId() {
		return id;
	}
	
	public String getRuleText() {
		return ruleText;
	}
	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}
	
	public void setCameoCode(String cameoCode) {
		this.cameoCode = cameoCode;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getCameoCode() {
		return cameoCode;
	}
	
	public String getWord() {
		return word;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public CameoRule(String ruleText,  String source) {
		super();
		this.ruleText = ruleText;
		
		this.source = source;
	}
	
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	
	public String getLangCode() {
		if (langCode == null) return "";
		return langCode;
	}
	
	public CameoRule() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return cameoCode+" "+word+" "+ruleText;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null){
			return false;
			
		} else if (obj instanceof CameoRule){
			CameoRule oRule = (CameoRule) obj;
			return cameoCode.equals(oRule.cameoCode) 
					&& ruleText.equals(oRule.ruleText)
					&& word.equals(oRule.word);
		} else {
			return false;
		}
	}
	
}