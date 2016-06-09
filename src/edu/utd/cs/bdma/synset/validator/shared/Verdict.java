package edu.utd.cs.bdma.synset.validator.shared;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Verdict implements Serializable{
	
	public static final int CORRECT = 0;
	public static final int INCORRECT = 1;
	public static final int AMBIGUOUS = 2;
	public static final int ADDITION = 3;

	@Id Long id;
	
	
	private String conceptId;
	
	private String lang;
	
	private String wordInLang;
	
	private Integer verdict;
	
	private String comment;
	
	
	public String getConceptId() {
		return conceptId;
	}
	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getWordInLang() {
		return wordInLang;
	}
	public void setWordInLang(String wordInLang) {
		this.wordInLang = wordInLang;
	}
	public int getVerdict() {
		return verdict;
	}
	public void setVerdict(int verdict) {
		this.verdict = verdict;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Verdict() {
		// TODO Auto-generated constructor stub
		verdict = Verdict.CORRECT;
	}

}
