package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class RuleTranslationHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id Long id;
	@Index Long translatedRuleId;
	@Index Long userId;
	@Index Date time;
	String ruleText;
	
	
	public Long getTranslatedRuleId() {
		return translatedRuleId;
	}
	public void setTranslatedRuleId(Long translatedRuleId) {
		this.translatedRuleId = translatedRuleId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getRuleText() {
		return ruleText;
	}
	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}
	public Long getId() {
		return id;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public RuleTranslationHistory() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
