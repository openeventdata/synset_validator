package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;


@Entity
public class SynsetEntry implements Serializable{

	@Id Long id;
	
	@Index Long idWord;
	String gloss;
	@Index String source;
	
	@Index Long submissionId;
	
	public Long getIdWord() {
		return idWord;
	}
	
    public void addInfo(Submission submission){
    	submissionId = submission.getId();
    	idWord = submission.getWordId();
    }
	
	public void setIdWord(Long idWord) {
		this.idWord = idWord;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getGloss() {
		return gloss;
	}
	public void setGloss(String gloss) {
		this.gloss = gloss;
	}
	public String getSource() {
		return source;
	}
	
	public String getDescription(){
		return gloss.substring(0, gloss.indexOf("\""));
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public SynsetEntry(String gloss, String source) {
		super();
		this.gloss = gloss;
		this.source = source;
	}
	
	public SynsetEntry() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
